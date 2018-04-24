package com.party.planner.controller.repository;

import com.party.planner.controller.domain.Guests;

import java.util.List;

public interface Repository {

    void addGuest(String firstname, String lastname, String gender);
    int addUser(String username, String password);
    boolean checkLogin(String username, String password);













    //
//    List<Guests> getGuest(List<String> firstname, List<String> lastname, List<String> gender);
//
//    List<Firstname> getFirstname;
//    List<Lastname> getLastname;
//    List<Gender> getGender;
//

}


//void createUser (String username, String password);