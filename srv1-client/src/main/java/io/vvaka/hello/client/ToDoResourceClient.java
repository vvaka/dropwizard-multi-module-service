package io.vvaka.hello.client;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseInterceptor;
import com.github.kristofa.brave.ServerSpan;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.okhttp.BraveOkHttpRequestResponseInterceptor;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.vvaka.hello.api.TodoResource;

/**
 * Created by vvaka on 8/2/16.
 */
public interface ToDoResourceClient {

    static TodoResource connect(Brave brave, String baseUrl) {

        // This stores the Original/Parent ServerSpan from Zipkin.
        final ServerSpan serverSpan = brave.serverSpanThreadBinder().getCurrentServerSpan();


        /* //Apache Http client  impl
        final CloseableHttpClient httpClient = HttpClients.custom()
                .addInterceptorFirst(new BraveHttpRequestInterceptor(brave.clientRequestInterceptor(),
                        new DefaultSpanNameProvider()))
                .addInterceptorFirst(new BraveHttpResponseInterceptor(brave.clientResponseInterceptor()))
                .build();

     */


        ObjectMapper mapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, true);
        mapper.registerModule(new Jdk8Module());

        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
                .addInterceptor(new BraveOkHttpRequestResponseInterceptor(
                        new ClientRequestInterceptor(brave.clientTracer()),
                        new ClientResponseInterceptor(brave.clientTracer()),
                        new DefaultSpanNameProvider())
                ).build();

        return Feign.builder().client(new OkHttpClient(okHttpClient))
                .retryer(Retryer.NEVER_RETRY)
                .requestInterceptor((t) -> {
                            brave.serverSpanThreadBinder().setCurrentSpan(serverSpan);
                        }
                )
                .encoder(new JacksonEncoder(mapper))
                .decoder(new JacksonDecoder(mapper))
                .logger(new Slf4jLogger(ToDoResourceClient.class))
                .contract(new JAXRSContract())
                .target(TodoResource.class, baseUrl);
    }


}
