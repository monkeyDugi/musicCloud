package com.musiccloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MustacheController {

    @GetMapping("/")
    public String dailyChart(Model model) {
        return "dailyChart";
    }
}
