package com.chilltime.planifyfront.model.transfer;

import java.time.LocalDate;
import java.time.LocalTime;

public class TEvent {

    public Long id;

    public String name;

    public LocalDate date;

    public LocalTime time;

    public String location;

    public TEvent(Long id, String name, LocalDate date, LocalTime time, String location) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public TEvent(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Event: " + name + " at " + time;
    }

}
