package com.kotor.reactstat.repository.impl;

import com.kotor.reactstat.domain.Event;
import com.kotor.reactstat.repository.RedisRepository;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private final RedisClient client = RedisClient.create("redis://localhost");
    private final RedisAsyncCommands<String, String> commands = client.connect().async();

    public List<Event> findAll() {
        List<String> result = null;
        try {
            result = commands.keys("*").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            String[] parts = result.get(i).split("!");
            events.add(new Event(parts[0], parts[1], parts[2]));
        }
        return events;
        
    }
    
    public List<Event> filterByDate(LocalDateTime start, LocalDateTime end, List<Event> events) {
        return events.stream().filter(event -> {
            LocalDateTime actual = LocalDateTime.parse(event.getDate());
            return actual.isBefore(end) && actual.isAfter(start);
        }).collect(Collectors.toList());
    }
    
    public int uniqVisitors(LocalDateTime start, LocalDateTime end, List<Event> events) {
        AtomicInteger i = new AtomicInteger(0);
        Set<String> visited = new HashSet<>();

        filterByDate(start, end, events).stream().forEach(event -> {
            if(!visited.contains(event.getRemoteAddr())) {
                i.incrementAndGet();
                visited.add(event.getRemoteAddr());
            }
        });
        return i.get();
    }

    public int favoriteUsers(LocalDateTime start, LocalDateTime end, List<Event> events) {
        Map<String, AtomicInteger> users = new HashMap<>();
        filterByDate(start, end, events).stream().forEach(event -> {
            if(users.containsKey(event.getId())) {
                users.get(event.getId()).incrementAndGet();
            } else {
                users.put(event.getId(), new AtomicInteger(0));
            }
        });
        AtomicInteger result = new AtomicInteger(0);
        users.forEach((id, seen) -> {
            if(seen.get() > 10) {
                result.incrementAndGet();
            }
        });
        return result.get();
    }
}
