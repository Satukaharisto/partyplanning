package com.party.planner.controller.controller;

import com.party.planner.controller.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private Repository repository;

    @GetMapping("/")
    public ModelAndView indexpage(){
        return new ModelAndView("index");
    }


    @GetMapping("/login")
    public String showLoginSite () {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView getInfoFromLoginForm(HttpSession session, @RequestParam String username, @RequestParam String password) {
        if (repository.checkLogin(username, password)) {
            session.setAttribute("user", username);
            return new ModelAndView("usersite");
        }
        return new ModelAndView("login");
    }

    @GetMapping("/usersite")
    public String secret(HttpSession session) {
        if (session.getAttribute("user") != null) {
            String username = (String) session.getAttribute("user");
            return "usersite";
        }
        return "login";
    }

    @PostMapping ("/usersite")
    public String logoutUser () {
        return "index";
    }

    @GetMapping("/logout")
    public String showLogoutSite (HttpSession session) {
        session.invalidate();
        return "index";
    }

    // denna fungerar och kan skapa users till SQL från formulär
    @PostMapping("/register")
    public String getInfoFromUserForm (@RequestParam String username,
                                        @RequestParam String password){
        repository.addUser(username, password);
        return "redirect:/usersite";                //Ska redirect till inloggat läge
    }

    @GetMapping("/register")
    public String registerUserSite(){
        return "register";
    }



    //    @GetMapping("/addguesttest")
//    @ResponseBody
//    public int addUser2 (){
//        int userId = repository.addUser("sandra", "hej");
//        return userId;
//    }

    // HÄR SKAPAR VI EN USER
//    @GetMapping("/adduser")
//    @ResponseBody
//    public ModelAndView addUser(){
//        int userId = repository.addUser();
//        return new ModelAndView("adduser");
//    }






//
//
//    // HÄR SKAPAR VI EN NY GÄST
//    @GetMapping("/addguest")
//    public ModelAndView addGuest(){
//        List<Guests> firstname = repository.getfirstname();
//        List<Guests> lastname = repository.getlastname();
//        List<Guests> gender = repository.getgender();
//
//        return new ModelAndView("guestlist")
//        .addObject("firstname", firstname)
//        .addObject("lastname", lastname)
//        .addObject("gender", lastname);
//    }
//
//    @PostMapping("/addguest")
//    public String getInfoFromGuestForm (@RequestParam String firstname,
//                                        @RequestParam String lastname,
//                                        @RequestParam String gender) {
//        repository.addGuest(firstname, lastname, gender);
//        return "redirect:/addguest";

//    }
}
