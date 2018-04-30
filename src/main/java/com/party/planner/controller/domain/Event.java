package com.party.planner.controller.domain;

import java.util.Date;

public class Event {
    private int id;
    private String name;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Event(int id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

}
