package com.party.planner.controller;

import com.party.planner.controller.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GuestController {

    @Autowired
    private Repository repository;

    @GetMapping("/addguesttest")
    @ResponseBody
    public int addUser2 (){
        int userId = repository.addUser("sandra", "hej");
        return userId;
    }

    // HÄR SKAPAR VI EN USER
    @GetMapping("/adduser")
    public ModelAndView addUser(){
        return new ModelAndView("adduser");
    }

    @PostMapping("/adduser")
    public String getInfoFromUserForm (@RequestParam String username,
                                        @RequestParam String password){
        int userId = repository.addUser(username, password);
        System.out.println(userId);
        return "redirect:/adduser";

    }

    // HÄR SKAPAR VI EN NY GÄST
    @GetMapping("/addguest")
    public ModelAndView addGuest(){
        return new ModelAndView("addguest");
    }

    @PostMapping("/addguest")
    public String getInfoFromGuestForm (@RequestParam String firstname,
                                        @RequestParam String lastname,
                                        @RequestParam String gender) {
        repository.addGuest(firstname, lastname, gender);
        return "redirect:/addguest";

    }
}
