package com.party.planner.controller.domain;

public class EventListModelMapper {
    // mappar ihop event och guest
    private EventListModelMapper() {
    }

    public static EventListModel map(Event event, Guest guest) {
        return new EventListModel(event.getId(),
                event.getName(),
                event.getDate(),
                guest == null ? null : guest.getId(),
                guest == null ? null : guest.getFirstname(),
                guest == null ? null : guest.getLastname(),
                guest == null ? null : guest.getGender());
    }
}
