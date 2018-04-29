package com.party.planner.controller.repository;

import com.party.planner.controller.domain.Budget;
import com.party.planner.controller.domain.Food;
import com.party.planner.controller.domain.Guest;
import com.party.planner.controller.domain.ToDo;

import java.sql.Date;
import java.util.List;

public interface Repository {

    int addGuest(String firstname, String lastname, String gender, int userId);
<<<<<<< Updated upstream
    int addUser(String username, String password);
=======

    int addUser(String username, String password, String email);

>>>>>>> Stashed changes
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
    List<ToDo> getChecklist(int userId);

}


