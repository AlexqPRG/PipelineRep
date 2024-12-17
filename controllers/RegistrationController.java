package com.web_project.zayavki.controllers;

/**
 * Controller for registration
 */

import com.web_project.zayavki.models.ClientModel;
import com.web_project.zayavki.models.RoleEnum;
import com.web_project.zayavki.models.UserModel;
import com.web_project.zayavki.repository.UserRepository;
import com.web_project.zayavki.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApiService apiService;

    /**
     * Endpoint for view registration form
     * @return name of html page
     */
    @GetMapping("/registration")
    public String registrationView(){
        return "registration";
    }

    /**
     * Edpoint for registration
     * @param user object of class UserModel
     * @param model object of class Model
     * @return rediret to login page
     */
    @PostMapping("/registration")
    public String registrationUser(UserModel user, Model model){
        if(userRepository.existsByUsername(user.getUsername())){
            model.addAttribute("message", "Пользователь уже существует");
            return "registration";

        }
        user.setActive(true);
        user.setRoles(Collections.singleton(RoleEnum.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setClient(new ClientModel());
        String json = apiService.setDataToApi("/user", user);
        return "redirect:/login";
    }
}
