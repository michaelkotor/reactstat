package com.kotor.reactstat.repository;

import com.kotor.reactstat.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface RedisRepository {
    List<Event> findAll();
    List<Event> filterByDate(LocalDateTime start, LocalDateTime end, List<Event> events);
    int uniqVisitors(LocalDateTime start, LocalDateTime end, List<Event> events);
    int favoriteUsers(LocalDateTime start, LocalDateTime end, List<Event> events);
}
