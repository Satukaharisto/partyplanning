package com.party.planner.controller.controller;

import com.party.planner.controller.domain.Budget;
import com.party.planner.controller.domain.Guest;
import com.party.planner.controller.domain.ToDo;
import com.party.planner.controller.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
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
        return new ModelAndView("login")
                .addObject("IncorrectPWorusername", "Password or username incorrect. Please try again.");
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
        if (!repository.userAlreadyExists(username)) {
            return new ModelAndView("register")
                    .addObject("InvalidInput", "Username already taken");
        }
        int userId = repository.addUser(username, password);
        session.setAttribute("userId", userId);
        session.setAttribute("user", username);

        return new ModelAndView("redirect:usersite");
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

        return "redirect:guestlist";
    }

    @GetMapping("/guestlist")
    public ModelAndView newGuestToList(HttpSession session) {

        List<Guest> guests = repository.getGuestList((int) session.getAttribute("userId"));

        return new ModelAndView("guestlist").addObject("guests", guests);                //Ska redirect till inloggat läge
    }

    @PostMapping("/budget")
    public String createBudget(@RequestParam String item,
                               @RequestParam int price,
                               HttpSession session) {
        repository.addBudgetItem(item, price, (int) session.getAttribute("userId"));

        return "redirect:budget";                //Ska redirect till inloggat läge
    }

    @GetMapping("/budget")
    public ModelAndView newBudgetItemToList(HttpSession session) {

        List<Budget> budgetList = repository.getBudgetList((int) session.getAttribute("userId"));

        return new ModelAndView("budget").addObject("budgetList", budgetList);                //Ska redirect till inloggat läge
    }

    @PostMapping("/checklist")
    public String createToDo(@RequestParam java.sql.Date date,
                             @RequestParam String toDo,
                             @RequestParam(required = false) Boolean done,
                             HttpSession session) {
        boolean b = false;
        if (done != null) {
            b = done;
        }
        repository.addToDo(date, toDo, b, (int) session.getAttribute("userId"));

        return "redirect:checklist";                //Ska redirect till inloggat läge
    }

    @GetMapping("/checklist")
    public ModelAndView newToDoToList(HttpSession session) {

        List<ToDo> checklist = repository.getChecklist((int) session.getAttribute("userId"));

        return new ModelAndView("checklist").addObject("checklist", checklist);                //Ska redirect till inloggat läge
    }
}