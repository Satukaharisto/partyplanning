package com.party.planner.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class seatingController {


    @GetMapping("/seatingarrangement")
    public ModelAndView seatingarrangement(){

        return new ModelAndView("seatingarrangement");
    }
}
