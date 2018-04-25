package com.party.planner.controller.repository;

import com.party.planner.controller.domain.Guest;

import java.util.List;

public interface Repository {

    int addGuest(String firstname, String lastname, String gender, int userId);

    int addUser(String username, String password);

    Integer checkLogin(String username, String password);

    List<Guest> getGuestList(int userId);

//    List<Guest> getGuest(List<String> firstname, List<String> lastname, List<String> gender);
//
//    List<Firstname> getFirstname;
//    List<Lastname> getLastname;
//    List<Gender> getGender;
//

}


//void createUser (String username, String password);