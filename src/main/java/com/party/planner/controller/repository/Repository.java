package com.party.planner.controller.repository;

import com.party.planner.controller.domain.Budget;
import com.party.planner.controller.domain.Food;
import com.party.planner.controller.domain.Guest;
import com.party.planner.controller.domain.Checklist;

import java.sql.Date;
import java.util.List;

public interface Repository {

    int addGuest(String firstname, String lastname, String gender, int userId);


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
    void updateBudget(int id, int userId, String item, int price);
    void updateChecklist (int id, int userId, Date date, String toDo, boolean done);

    void deleteBudget(int id);
    void deleteChecklist(int id);
    void deleteFoodPreference(int id);
    void deleteGuest(int id);

    List<Guest> getGuestList(int userId);
    Food getFoodPreference(int guestId);
    List<Budget> getBudgetList(int userId);
    List<Checklist> getChecklist(int userId);

}


