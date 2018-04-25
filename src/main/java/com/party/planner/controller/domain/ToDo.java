package com.party.planner.controller.domain;


import java.util.Date;

public class ToDo {
    private int id;
    private Date date;
    private String toDo;
    private boolean done;

    public ToDo(int id, Date date, String toDo, boolean done) {
        this.id = id;
        this.date = date;
        this.toDo = toDo;
        this.done = done;
    }

}
