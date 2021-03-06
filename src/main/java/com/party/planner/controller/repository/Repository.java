package com.party.planner.controller.repository;

import com.party.planner.controller.domain.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface Repository {

    int addGuest(int userId, String firstname, String lastname, String email, String gender);


    int addUser(String username, String email, String password);
    int addBudgetItem(String item, int price, int eventId);
    int addToDo(Date date, String toDo, boolean done, int userId);
    int addFoodPreference(int guestId, String allergy, String foodPreference, String alcohol);
    int addEvent(String name, Date date, int userId);

//    void changeBudgetItemPrice (int userId, String item, int price);

    Integer checkLogin(String username, String password);

    boolean userAlreadyExists(String username);
    boolean budgetItemAlreadyExists(String item, int userId);

    int budgetSum(int eventId);

    void updateGuest(int eventId, int id, String firstname, String lastname, String email, String gender);
    void updateFoodPreference(int id, int guestId, String allergy, String foodPreference, String alcohol);
    void updateBudget(int eventId, int id, String item, int price);
    void updateChecklist (int eventId, int id, Date date, String toDo, boolean done);
    void updateEvent(int eventId, String eventName, Date eventDate, int userId);

    void deleteBudget(int id);
    void deleteChecklist(int id);
    void deleteFoodPreference(int id);
    void deleteFoodPreferenceByGuestId(int id);
    void deleteGuest(int id);
    void deleteEvent(int id);
    void deleteGuestsByEventId(int id);

    Food getFoodPreference(int guestId);

    List<Guest> getGuestList(int eventId);
    List<Budget> getBudgetList(int eventId);
    List<Checklist> getChecklist(int eventId);
    List<Event> getEventList(int userId);

    List<Inspiration> listInspiration();

//    Guest getGuests(int eventId);
}


