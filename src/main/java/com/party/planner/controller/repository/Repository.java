package com.party.planner.controller.repository;

import com.party.planner.controller.domain.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface Repository {

    int addGuest( int eventId, String firstname, String lastname, String gender);


    int addUser(String username, String password, String email);
    int addBudgetItem(String item, int price, int userId);
    int addToDo(Date date, String toDo, boolean done, int userId);

    int addFoodPreference(int guestId, String allergy, String foodPreference, String alcohol);

//    void changeBudgetItemPrice (int userId, String item, int price);

    Integer checkLogin(String username, String password);

    boolean userAlreadyExists(String username);
    boolean budgetItemAlreadyExists(String item, int userId);

    int budgetSum(int userId);

    void updateGuest(int id, int userId, String firstname, String lastname, String gender);
    void updateFoodPreference(int id, int guestId, String allergy, String foodPreference, String alcohol);

    List<Guest> getGuestList(int userId);
    Food getFoodPreference(int guestId);
    List<Budget> getBudgetList(int userId);
    List<ToDo> getChecklist(int eventId);
    int addEvent(String name, Date date, int userId);
    List<Event> getEventList(int userId);
    List<Inspiration> listInspiration();

//    Guest getGuests(int eventId);
}


