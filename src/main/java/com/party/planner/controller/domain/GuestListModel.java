package com.party.planner.controller.domain;

public class GuestListModel {

    private int id;
    private Integer foodId;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private String allergy;
    private String alcohol;
    private String foodPreference;



    public GuestListModel(int id, Integer foodId, String firstname, String lastname, String email, String gender, String allergy, String alcohol, String foodPreference) {
        this.id = id;
        this.foodId = foodId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.allergy = allergy;
        this.alcohol = alcohol;
        this.foodPreference = foodPreference;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
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

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getFoodPreference() {
        return foodPreference;
    }

    public void setFoodPreference(String foodPreference) {
        this.foodPreference = foodPreference;
    }

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}
}
