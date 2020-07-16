package com.kotor.reactstat.handler;

import com.kotor.reactstat.domain.Event;
import com.kotor.reactstat.domain.Result;
import com.kotor.reactstat.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class GreetingHandler {
    @Autowired
    private  RedisRepository redisRepository;
    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, Event> coffeeOps;
    private final ReactiveRedisOperations<String, String> stringOps;

    public GreetingHandler(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Event> coffeeOps, ReactiveRedisOperations<String, String> stringOps) {
        this.factory = factory;
        this.coffeeOps = coffeeOps;
        this.stringOps = stringOps;
    }

    public Mono<ServerResponse> createEvent(ServerRequest request) {
        String remoteAddr = request.remoteAddress().get().getAddress().getHostAddress();
        String id = request.queryParam("id").orElse("0");

        Event happened = new Event(id, remoteAddr);

        factory.getReactiveConnection().serverCommands().info().thenMany(
                Flux.just(remoteAddr)
                    .map(name -> new Event(id, remoteAddr))
                .flatMap(event -> stringOps.opsForValue().set(event.getRemoteAddr() + "!" +  event.getId() + "!" + event.getDate(), "")))
                .subscribe();

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(happened);
    }

    public Mono<ServerResponse> generateReport(ServerRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.from(formatter.parse(request
                .queryParam("start").
                        orElse(LocalDateTime.of(2020, 7, 1, 0, 0, 0).toString())));

        LocalDateTime end = LocalDateTime.from(formatter.parse(request
                .queryParam("end")
                .orElse((LocalDateTime.now()).truncatedTo(ChronoUnit.SECONDS).toString())));



        List<Event> allEvents = redisRepository.findAll();
        Result result = new Result(
                redisRepository.filterByDate(start, end, allEvents),
                redisRepository.uniqVisitors(start, end, allEvents),
                redisRepository.favoriteUsers(start, end, allEvents)
        );

        Flux<Result> toSend = Flux.just(result).map(res -> new Result(res.getFiltered(), res.getNumberOfUniqVisitors(), res.getNumberOfFavoriteVisitors()));

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(toSend, Result.class);
    }
}
