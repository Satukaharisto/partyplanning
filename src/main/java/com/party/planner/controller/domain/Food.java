package com.party.planner.controller.domain;

public class Food {

    private int id;                     // foodId
    private String allergy;            //gluten, lactos, nuts, other
    private String alcohol;            // yes or no
    private String foodPreference;      // vegetarian, vegan, no fish?

    public Food(int id, String allergy, String alcohol, String foodPreference) {
        this.id = id;
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
}
