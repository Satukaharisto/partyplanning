package com.party.planner.controller.domain;

public class Budget {
    private int id;
    private String item;
    private int price;

    public Budget(int id, String item, int price) {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    public Budget(int id) {
        this.id = id;
    }
}
