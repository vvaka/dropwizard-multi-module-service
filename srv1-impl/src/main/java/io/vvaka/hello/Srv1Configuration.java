package io.vvaka.hello;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smoketurner.dropwizard.zipkin.ZipkinFactory;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import java.util.List;

public class Srv1Configuration extends Configuration {
    // TODO: implement service configuration

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @JsonProperty("zipkin")
    public ZipkinFactory zipkinFactory;

    @JsonProperty("serviceDependencies")
    public List<String> serviceDependencies;

}
