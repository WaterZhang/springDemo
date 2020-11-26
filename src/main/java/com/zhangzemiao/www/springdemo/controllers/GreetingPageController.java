package com.zhangzemiao.www.springdemo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GreetingPageController {

    @GetMapping("/greeting.html")
    public ModelAndView greeting(
        @RequestParam(name="name", required = false, defaultValue = "World") String name){

        final ModelMap map = new ModelMap();
        map.put("name", name);
        return new ModelAndView("greeting", map);
    }

}
