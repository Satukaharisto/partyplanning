package com.party.planner.controller.domain;

import java.util.Date;

public class EventListModel {
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

    public Integer getGuestId() {
        return guestId;
    }

    public void setGuestId(Integer guestId) {
        this.guestId = guestId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private Integer guestId;
    private String firstname;
    private String lastname;
    private String gender;

    public EventListModel(int id, String name, Date date, Integer guestId, String firstname, String lastname, String gender) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.guestId = guestId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
    }


}
