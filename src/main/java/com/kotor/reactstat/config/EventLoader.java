package com.kotor.reactstat.config;

import com.kotor.reactstat.domain.Event;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class EventLoader {
    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, Event> coffeeOps;

    public EventLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Event> coffeeOps) {
        this.factory = factory;
        this.coffeeOps = coffeeOps;
    }

    @PostConstruct
    public void loadData() {
        factory.getReactiveConnection().serverCommands().info().thenMany(
                Flux.just("78!google.com!2020-07-07T12:12:13", "7!facebook!2020-08-07T12:22:53", "1234!kotor.com!2020-07-17T09:12:13")
                        .map(name -> new Event(name.split("!")[0], name.split("!")[1], name.split("!")[2]))
                        .flatMap(event -> coffeeOps.opsForValue().set(event.getId()  + "!" + event.getRemoteAddr() + "!" + event.getDate(), event)))
                .thenMany(coffeeOps.keys("*")
                        .flatMap(coffeeOps.opsForValue()::get))
                .subscribe(object -> System.out.println(object.getDate()));
    }
}