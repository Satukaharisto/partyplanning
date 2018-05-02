package com.party.planner.controller.controller;

import com.party.planner.controller.domain.*;
import com.party.planner.controller.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.Date;
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
    public ModelAndView newEventList(HttpSession session) {
        List<Event> eventlist = repository.getEventList((int) session.getAttribute("userId"));
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
    public ModelAndView seatingarrangement(HttpSession session) {
        List<Guest> guests = repository.getGuestList((int) session.getAttribute("userId"));
//        List<GuestListModel> guestList = new ArrayList<>();
//        for (Guest guest : guests) {
//            Food food = repository.getFoodPreference(guest.getId());
//            guestList.add(GuestListModelMapper.map(guest, food));
//        }
        return new ModelAndView("seatingarrangement").addObject("guestList", guests);
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
        repository.updateBudget(id, price, item, eventId);
        return new ModelAndView("redirect:budget?eventId=" + eventId);
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
    public ModelAndView deleteChecklist (@RequestParam int eventId, @RequestParam int id) {
        repository.deleteChecklist(id);
        return new ModelAndView("redirect:checklist?eventId=" + eventId);
    }

    @PostMapping("/updateChecklist")
    public ModelAndView updateChecklist(
            @RequestParam int id,
            @RequestParam Date date,
            @RequestParam String toDo,
            @RequestParam(required = false) Boolean done,
            HttpSession session) {
        boolean checked = false;
        if (done != null){
            checked = done;
        }
        repository.updateChecklist(id, (int) session.getAttribute("userId"), date, toDo, checked);
        return new ModelAndView("redirect:checklist");
    }
}
