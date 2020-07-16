package com.kotor.reactstat.domain;

import java.util.List;

public class Result {
    private List<Event> filtered;
    private int numberOfUniqVisitors;
    private int numberOfFavoriteVisitors;

    public Result(List<Event> filtered, int numberOfUniqVisitors, int numberOfFavoriteVisitors) {
        this.filtered = filtered;
        this.numberOfUniqVisitors = numberOfUniqVisitors;
        this.numberOfFavoriteVisitors = numberOfFavoriteVisitors;
    }

    public List<Event> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<Event> filtered) {
        this.filtered = filtered;
    }

    public int getNumberOfUniqVisitors() {
        return numberOfUniqVisitors;
    }

    public void setNumberOfUniqVisitors(int numberOfUniqVisitors) {
        this.numberOfUniqVisitors = numberOfUniqVisitors;
    }

    public int getNumberOfFavoriteVisitors() {
        return numberOfFavoriteVisitors;
    }

    public void setNumberOfFavoriteVisitors(int numberOfFavoriteVisitors) {
        this.numberOfFavoriteVisitors = numberOfFavoriteVisitors;
    }
}
