package com.party.planner.controller.controller;

import com.party.planner.controller.domain.Guest;
import com.party.planner.controller.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private Repository repository;

    @GetMapping("/")
    public ModelAndView indexpage() {
        return new ModelAndView("index");
    }

    @GetMapping("/login")
    public String showLoginSite() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView getInfoFromLoginForm(HttpSession session, @RequestParam String username, @RequestParam String password) {
        Integer userId = repository.checkLogin(username, password);
        if (userId != null) {
            session.setAttribute("user", username);
            session.setAttribute("userId", userId);
            return new ModelAndView("usersite");
        }
        return new ModelAndView("login");
    }

    @GetMapping("/usersite")
    public String secret(HttpSession session) {
        if (session.getAttribute("user") != null) {
            //session.getAttribute("user");
            return "usersite";
        }
        return "login";
    }

    @PostMapping("/usersite")
    public String logoutUser() {
        return "index";
    }

    @GetMapping("/logout")
    public String showLogoutSite(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // denna fungerar och kan skapa users till SQL från formulär
    @PostMapping("/register")
    public ModelAndView createUser(HttpSession session, @RequestParam String username,
                             @RequestParam String password) {
      //  boolean alreadyExists = repository.userAlreadyExists(username);
        if (!repository.userAlreadyExists(username)) {
            return new ModelAndView("register")
                    .addObject("InvalidInput", "Username already taken");
        }
            int userId = repository.addUser(username, password);
            session.setAttribute("userId", userId);
            session.setAttribute("user", username);

            return new ModelAndView("redirect:usersite");                //Ska redirect till inloggat läge
        }



    @GetMapping("/register")
    public String registerUserSite() {
        return "register";
    }

    @PostMapping("/guestlist")
    public String createGuest(@RequestParam String firstname,
                              @RequestParam String lastname,
                              @RequestParam String gender,
                              HttpSession session) {
        repository.addGuest(firstname, lastname, gender, (int) session.getAttribute("userId"));

        return "redirect:guestlist";                //Ska redirect till inloggat läge
    }

    @GetMapping("/guestlist")
    public ModelAndView newGuestToList(HttpSession session) {

        List<Guest> guests = repository.getGuestList((int) session.getAttribute("userId"));

        return new ModelAndView("guestlist").addObject("guests", guests);                //Ska redirect till inloggat läge
    }
}