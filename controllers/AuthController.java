package com.web_project.zayavki.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model){
        if(error != null){
            model.addAttribute("errorMessage", "Неверное имя пользователя или пароль");
        }
        return "login";
    }
}
