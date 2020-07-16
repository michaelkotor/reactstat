package com.kotor.reactstat.config;

import com.kotor.reactstat.handler.GreetingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class GreetingRouter {

    @Bean
    public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler) {

        return RouterFunctions
                .route(RequestPredicates.GET("/event")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                                    greetingHandler::createEvent)

                .andRoute(RequestPredicates.GET("/events")
                    .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        greetingHandler::generateReport);
    }
}
