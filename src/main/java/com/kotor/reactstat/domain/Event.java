package com.kotor.reactstat.domain;

import java.time.LocalDateTime;

public class Event {
    private String id;
    private String remoteAddr;
    private String date;

    public Event() {
        this.date = LocalDateTime.now().toString();
    }

    public Event(String remoteAddr) {
        this.remoteAddr = remoteAddr;
        this.date = LocalDateTime.now().toString();
    }

    public Event(String id, String remoteAddr) {
        this.id = id;
        this.remoteAddr = remoteAddr;
        this.date = LocalDateTime.now().toString();
    }

    public Event(String id, String remoteAddr, String date) {
        this.id = id;
        this.remoteAddr = remoteAddr;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
