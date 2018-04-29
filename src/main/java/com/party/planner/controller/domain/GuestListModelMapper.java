package com.party.planner.controller.domain;

public class GuestListModelMapper {
// mappar ihop guest och food
    private GuestListModelMapper() {
    }

    public static GuestListModel map(Guest guest, Food food) {
        return new GuestListModel(guest.getId(),
                food == null ? null : food.getId(),
                guest.getFirstname(),
                guest.getLastname(),
                guest.getGender(),
                food == null ? null : food.getAllergy(),
                food == null ? null : food.getAlcohol(),
                food == null ? null : food.getFoodPreference());
    }
}
