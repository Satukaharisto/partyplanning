package com.party.planner.controller.repository;

import com.party.planner.controller.domain.Budget;
import com.party.planner.controller.domain.Guest;
import com.party.planner.controller.domain.ToDo;

import java.sql.Date;
import java.util.List;

public interface Repository {

    int addGuest(String firstname, String lastname, String gender, int userId);

    int addUser(String username, String password);

    int addBudgetItem (String item, int price, int userId);
   int addToDo(Date date, String toDo, boolean done, int userId);

    Integer checkLogin(String username, String password);

    List<Guest> getGuestList(int userId);

    List<Budget> getBudgetList (int userId);

    List<ToDo> getChecklist(int userId);

//    List<Guest> getGuest(List<String> firstname, List<String> lastname, List<String> gender);
//
//    List<Firstname> getFirstname;
//    List<Lastname> getLastname;
//    List<Gender> getGender;
//

}


//void createUser (String username, String password);