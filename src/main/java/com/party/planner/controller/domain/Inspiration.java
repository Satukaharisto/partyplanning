package com.party.planner.controller.domain;

public class Inspiration {
    private int inspirationId;
    private String category;
    private String name;
    private String text;
    private String link;
    private String picture;

    public int getInpirationId() {
        return inspirationId;
    }

    public void setInpirationId(int inpirationId) {
        this.inspirationId = inpirationId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Inspiration(int inspirationId, String category, String name, String text, String link, String picture) {
        this.inspirationId = inspirationId;
        this.category = category;
        this.name = name;
        this.text = text;
        this.link = link;
        this.picture = picture;
    }
}
