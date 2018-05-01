package com.party.planner.controller.domain;


import java.util.Date;

public class Checklist {
    private int id;
    private Date date;
    private String toDo;
    private boolean done;

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getToDo() {
        return toDo;
    }

    public boolean isDone() {
        return done;
    }

    public Checklist(int id, Date date, String toDo, boolean done) {
        this.id = id;
        this.date = date;
        this.toDo = toDo;
        this.done = done;
    }

}
