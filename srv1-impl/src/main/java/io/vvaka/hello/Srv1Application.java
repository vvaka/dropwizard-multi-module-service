package io.vvaka.hello;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.kristofa.brave.Brave;
import com.smoketurner.dropwizard.zipkin.ZipkinBundle;
import com.smoketurner.dropwizard.zipkin.ZipkinFactory;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.vvaka.hello.resources.ToDoResourceImpl;

public class Srv1Application extends Application<Srv1Configuration> {

    public static void main(final String[] args) throws Exception {
        new Srv1Application().run(args);
    }

    @Override
    public String getName() {
        return "srv1-impl";
    }

    @Override
    public void initialize(final Bootstrap<Srv1Configuration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<Srv1Configuration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(Srv1Configuration srv1Configuration) {
                return srv1Configuration.swaggerBundleConfiguration;
            }
        });


        bootstrap.addBundle(new ZipkinBundle<Srv1Configuration>(getName()) {
            @Override
            public ZipkinFactory getZipkinFactory(Srv1Configuration configuration) {
                return configuration.zipkinFactory;
            }
        });

    }

    @Override
    public void run(final Srv1Configuration configuration,
                    final Environment environment) {
        // TODO: implement application

        Brave brave = configuration.zipkinFactory.build(environment);

        environment.getObjectMapper().registerModule(new GuavaModule());
        environment.getObjectMapper().registerModule(new Jdk8Module());

        environment.jersey().register(new ToDoResourceImpl(brave));
    }


}
