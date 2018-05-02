package com.party.planner.controller.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.party.planner.controller.domain.*;
import com.party.planner.controller.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
            @RequestParam String password) {
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
    public String createEvent(@RequestParam String name,
                             @RequestParam java.sql.Date date,
                             HttpSession session) {
        repository.addEvent(name, date,(int) session.getAttribute("userId"));
        return "redirect:event";                //Ska redirect till inloggat läge
    }

    @GetMapping("/event")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public ModelAndView newEventList(HttpSession session) {
        List<Event> eventlist = repository.getEventList((int) session.getAttribute("userId"));
//        List<EventListModel> eventlist2 = new ArrayList<>();
//        for (Event event : eventlist) {
//            Guest guest = repository.getGuests(event.getId());
//            eventlist2.add(EventListModelMapper.map(event, guest));
//        }
        return new ModelAndView("event").addObject("eventlist", eventlist);
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


    @PostMapping("/register")

    public ModelAndView createUser(HttpSession session, @RequestParam String username,
                                   @RequestParam String password, @RequestParam String email) {

        if (!repository.userAlreadyExists(username)) {
            return new ModelAndView("index")
                    .addObject("InvalidInput", "Username already taken");
        }
        int userId = repository.addUser(username, password, email);
        session.setAttribute("userId", userId);
        session.setAttribute("user", username);
        session.setAttribute("user", email);

        return new ModelAndView("redirect:event");
    }


    @PostMapping("/guestlist")
    public String createGuest(@RequestParam int eventId,
                              @RequestParam String firstname,
                              @RequestParam String lastname,
                              @RequestParam String gender,
                              @RequestParam(required = false) String allergy,
                              @RequestParam(required = false) String foodPreference,
                              @RequestParam(required = false) String alcohol) {
        int guestId = repository.addGuest(eventId, firstname, lastname, gender);
        repository.addFoodPreference(guestId, allergy, foodPreference, alcohol);

        return "redirect:guestlist";
    }

    @PostMapping("/food")
    public String addFoodPreference(@RequestParam int guestId,
                                    @RequestParam String allergie,
                                    @RequestParam String foodPreference,
                                    @RequestParam String alcohol) {
        repository.addFoodPreference(guestId, allergie, foodPreference, alcohol);
        return "redirect:guestlist";
    }

    @GetMapping("/guestlist")
    public ModelAndView newGuestToList(HttpSession session) {
        List<Guest> guests = repository.getGuestList((int) session.getAttribute("eventId"));
        List<GuestListModel> guestList = new ArrayList<>();
        for (Guest guest : guests) {
            Food food = repository.getFoodPreference(guest.getId());
            guestList.add(GuestListModelMapper.map(guest, food));
        }
        return new ModelAndView("guestlist").addObject("guestList", guestList);
    }

    @GetMapping("/seatingarrangement")
    public ModelAndView seatingarrangement (HttpSession session) {
        List<Guest> guests = repository.getGuestList((int) session.getAttribute("userId"));
//        List<GuestListModel> guestList = new ArrayList<>();
//        for (Guest guest : guests) {
//            Food food = repository.getFoodPreference(guest.getId());
//            guestList.add(GuestListModelMapper.map(guest, food));
//        }
        return new ModelAndView("seatingarrangement").addObject("guestList", guests);
    }

    @PostMapping("/budget")
    public ModelAndView createBudget(@RequestParam String item,
                                     @RequestParam int price,
                                     HttpSession session) {
        if (repository.budgetItemAlreadyExists(item, (int) session.getAttribute("userId"))) {
            List<Budget> budgetList = repository.getBudgetList((int) session.getAttribute("userId"));
            int total = repository.budgetSum((int) session.getAttribute("userId"));
            return new ModelAndView("budget").addObject("InvalidInput", "Budget item already exists")
                    .addObject("budgetList", budgetList).addObject("total", total);
        }
        repository.addBudgetItem(item, price, (int) session.getAttribute("userId"));

        return new ModelAndView("redirect:budget");
    }

    @GetMapping("/budget")
    public ModelAndView newBudgetItemToList(HttpSession session) {
        List<Budget> budgetList = repository.getBudgetList((int) session.getAttribute("userId"));
        int total = repository.budgetSum((int) session.getAttribute("userId"));
        return new ModelAndView("budget").addObject("budgetList", budgetList)
                .addObject("total", total);                //Ska redirect till inloggat läge
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
        List<ToDo> checklist = repository.getChecklist(eventId);
        return new ModelAndView("checklist").addObject("checklist", checklist).
                addObject("eventId", eventId);                //Ska redirect till inloggat läge
    }

    @PostMapping("/updateGuest")
    public ModelAndView updateGuest(
            @RequestParam int guestId,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String gender,
            @RequestParam int foodId,
            @RequestParam(required = false) String allergy,
            @RequestParam(required = false) String foodPreference,
            @RequestParam(required = false) String alcohol,
            HttpSession session) {
        repository.updateGuest(guestId, (int) session.getAttribute("userId"), firstname, lastname, gender);
        repository.updateFoodPreference(foodId, guestId, allergy, foodPreference, alcohol);
        return new ModelAndView("redirect:guestlist");
    }
    @GetMapping("/inspiration")
    public ModelAndView listInspirationItems() {
        return new ModelAndView("inspiration")
                .addObject("inspirationItems", repository.listInspiration());
    }
}
