package com.party.planner.controller.controller;

import com.party.planner.controller.domain.*;
import com.party.planner.controller.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private Repository repository;


    @GetMapping("/")
    public ModelAndView indexpage() {
        return new ModelAndView("index");
    }

    @PostMapping("/login")
    public ModelAndView getInfoFromLoginForm(
            HttpSession session,
            @RequestParam String username,
            @RequestParam String password,
            String hashedPassword) {
        Integer userId = repository.checkLogin(username, password);

        if (userId != null) {
            session.setAttribute("user", username);
            session.setAttribute("userId", userId);

            return new ModelAndView("redirect:event");

        }

        return new ModelAndView("index")
                .addObject("IncorrectPWorusername", "Password or username incorrect. Please try again.");
    }

    /*-----------------------------------------------------------*/
    @PostMapping("/event")
    public ModelAndView createEvent(@RequestParam String name,
                                    @RequestParam java.sql.Date date,
                                    HttpSession session) {
        repository.addEvent(name, date, (int) session.getAttribute("userId"));
        return new ModelAndView("redirect:event");                //Ska redirect till inloggat läge

    }

    @GetMapping("/event")
    public ModelAndView newEventList(HttpSession session) {
        List<Event> eventlist = repository.getEventList((int) session.getAttribute("userId"));
        return new ModelAndView("event").addObject("eventlist", eventlist);
    }

    @GetMapping("/usersite")
    public ModelAndView secret(HttpSession session) {
        if (session.getAttribute("user") != null) {
            //session.getAttribute("user");
            return new ModelAndView("usersite");
        }
        return new ModelAndView("login");
    }

    @PostMapping("/usersite")
    public ModelAndView logoutUser() {
        return new ModelAndView("index");
    }

    @GetMapping("/logout")
    public ModelAndView showLogoutSite(HttpSession session) {
        session.invalidate();
        return new ModelAndView("index");
    }


    @PostMapping("/register")

    public ModelAndView createUser(HttpSession session, @RequestParam String username, @RequestParam String email,
                                   @RequestParam String password) {
        if (!repository.userAlreadyExists(username)) {
            return new ModelAndView("index")
                    .addObject("InvalidInput", "Username already taken");
        }
        int userId = repository.addUser(username, password, email);
        session.setAttribute("userId", userId);
        session.setAttribute("user", email);
        session.setAttribute("user", username);

        return new ModelAndView("redirect:event");
    }


    @PostMapping("/guestlist")
    public ModelAndView createGuest(@RequestParam int eventId,
                                    @RequestParam String firstname,
                                    @RequestParam String lastname,
                                    @RequestParam String email,
                                    @RequestParam String gender,
                                    @RequestParam(required = false) String allergy,
                                    @RequestParam(required = false) String foodPreference,
                                    @RequestParam(required = false) String alcohol) {
        int guestId = repository.addGuest(eventId, firstname, lastname, email, gender);
        repository.addFoodPreference(guestId, allergy, foodPreference, alcohol);

        return new ModelAndView("redirect:guestlist?eventId=" + eventId);
    }

    @PostMapping("/food")
    public ModelAndView addFoodPreference(@RequestParam int eventId,
                                          @RequestParam int guestId,
                                          @RequestParam String allergie,
                                          @RequestParam String foodPreference,
                                          @RequestParam String alcohol) {
        repository.addFoodPreference(guestId, allergie, foodPreference, alcohol);
        return new ModelAndView("redirect:guestlist?eventId=" + eventId);
    }

    @GetMapping("/guestlist")
    public ModelAndView newGuestToList(@RequestParam int eventId) {
        List<Guest> guests = repository.getGuestList(eventId);
        List<GuestListModel> guestList = new ArrayList<>();
        for (Guest guest : guests) {
            Food food = repository.getFoodPreference(guest.getId());
            guestList.add(GuestListModelMapper.map(guest, food));
        }
        return new ModelAndView("guestlist").addObject("eventId", eventId).addObject("guestList", guestList);
    }

    @GetMapping("/seatingarrangement")
    public ModelAndView seatingarrangement(@RequestParam int eventId) {
        List<Guest> guests = repository.getGuestList(eventId);
//        List<GuestListModel> guestList = new ArrayList<>();
//        for (Guest guest : guests) {
//            Food food = repository.getFoodPreference(guest.getId());
//            guestList.add(GuestListModelMapper.map(guest, food));
//        }
        return new ModelAndView("seatingarrangement").addObject("guestList", guests).addObject("eventId", eventId);
    }

    @PostMapping("/budget")
    public ModelAndView createBudget(@RequestParam int eventId,
                                     @RequestParam String item,
                                     @RequestParam int price) {
        if (repository.budgetItemAlreadyExists(item, eventId)) {
            List<Budget> budgetList = repository.getBudgetList(eventId);
            int total = repository.budgetSum(eventId);
            return new ModelAndView("budget").addObject("InvalidInput", "Budget item already exists")
                    .addObject("budgetList", budgetList).addObject("total", total).addObject("eventId", eventId);
        }
        repository.addBudgetItem(item, price, eventId);

        return new ModelAndView("redirect:budget?eventId=" + eventId);
    }

    @GetMapping("/budget")
    public ModelAndView newBudgetItemToList(@RequestParam int eventId) {
        List<Budget> budgetList = repository.getBudgetList(eventId);
        int total = repository.budgetSum(eventId);
        return new ModelAndView("budget").addObject("budgetList", budgetList)
                .addObject("total", total).addObject("eventId", eventId);                //Ska redirect till inloggat läge
    }

    @PostMapping("/checklist")
    public ModelAndView createToDo(@RequestParam int eventId, @RequestParam java.sql.Date date,
                                   @RequestParam String toDo,
                                   @RequestParam(required = false) Boolean done) {
        boolean b = false;
        if (done != null) {
            b = done;
        }
        repository.addToDo(date, toDo, b, eventId);
        return new ModelAndView("redirect:checklist?eventId=" + eventId);                //Ska redirect till inloggat läge
    }

    @GetMapping("/checklist")
    public ModelAndView newToDoToList(@RequestParam int eventId) {
        List<Checklist> checklist = repository.getChecklist(eventId);
        return new ModelAndView("checklist").addObject("checklist", checklist).
                addObject("eventId", eventId);                //Ska redirect till inloggat läge
    }

    @PostMapping("/updateGuest")
    public ModelAndView updateGuest(
            @RequestParam int eventId,
            @RequestParam int guestId,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String gender,
            @RequestParam int foodId,
            @RequestParam(required = false) String allergy,
            @RequestParam(required = false) String foodPreference,
            @RequestParam(required = false) String alcohol) {
        repository.updateGuest(eventId, guestId, firstname, lastname, email, gender);
        repository.updateFoodPreference(foodId, guestId, allergy, foodPreference, alcohol);
        return new ModelAndView("redirect:guestlist?eventId=" + eventId);
    }

    @PostMapping("/updateBudget")
    public ModelAndView updateBudget(
            @RequestParam int id,
            @RequestParam String item,
            @RequestParam int price,
            @RequestParam int eventId) {
        repository.updateBudget(eventId, id, item, price);
        return new ModelAndView("redirect:budget?eventId=" + eventId);
    }

    @GetMapping("/inspiration")
    public ModelAndView listInspirationItems() {
        return new ModelAndView("inspiration")
                .addObject("inspirationItems", repository.listInspiration());
    }

    @GetMapping("/deleteBudget")
    public ModelAndView deleteBudget(@RequestParam int eventId, @RequestParam int id) {
        repository.deleteBudget(id);
        return new ModelAndView("redirect:budget?eventId=" + eventId);
    }

    @GetMapping("/deleteGuest")
    public ModelAndView deleteGuest(@RequestParam int eventId, @RequestParam int guestId) {
        Food food = repository.getFoodPreference(guestId);
        repository.deleteFoodPreference(food.getId());

        repository.deleteGuest(guestId);
        return new ModelAndView("redirect:guestlist?eventId=" + eventId);
    }

    @GetMapping("/deleteChecklist")
    public ModelAndView deleteChecklist(@RequestParam int eventId, @RequestParam int id) {
        repository.deleteChecklist(id);
        return new ModelAndView("redirect:checklist?eventId=" + eventId);
    }

    @GetMapping("/deleteEvent")
    public ModelAndView deleteEvent(@RequestParam int id) {
        for (Guest guest : repository.getGuestList(id)) {
            repository.deleteFoodPreferenceByGuestId(guest.getId());
        }

        for (Budget budget : repository.getBudgetList(id)) {
            repository.deleteBudget(budget.getId());
        }

        for (Checklist checklist : repository.getChecklist(id)) {
            repository.deleteChecklist(checklist.getId());
        }

        repository.deleteGuestsByEventId(id);
        repository.deleteEvent(id);
        return new ModelAndView("redirect:event");
    }


    @PostMapping("/updateChecklist")
    public ModelAndView updateChecklist(
            @RequestParam int id,
            @RequestParam Date date,
            @RequestParam String toDo,
            @RequestParam(required = false) Boolean done,
            @RequestParam int eventId) {
        boolean checked = false;
        if (done != null) {
            checked = done;
        }
        repository.updateChecklist(id, eventId, date, toDo, checked);
        return new ModelAndView("redirect:checklist?eventId=" + eventId);
    }


    @PostMapping("/updateEvent")
    public ModelAndView updateEvent(
            @RequestParam int eventId,
            @RequestParam String eventName,
            @RequestParam Date eventDate,
            @RequestParam int userId) {
        repository.updateEvent(eventId, eventName, eventDate, userId);
        return new ModelAndView("redirect:event?userId=" + userId);
    }
}