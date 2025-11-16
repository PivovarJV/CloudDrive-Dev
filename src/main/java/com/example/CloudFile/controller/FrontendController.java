package com.example.CloudFile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {
    @GetMapping(value = {"/files/**", "/registration", "/login"})
    public String forward() {
        return "forward:/index.html";
    }
}
