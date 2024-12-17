package com.web_project.zayavki.controllers.modelControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.web_project.zayavki.models.AutoModel;
import com.web_project.zayavki.models.ClientModel;
import com.web_project.zayavki.models.LogEntry;
import com.web_project.zayavki.service.ApiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping("/log")
public class LogController {
    @Autowired
    private ApiService apiService;

    private final String url = "/log";

    @GetMapping("/all")
    public String getLog(Model model){

        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url);

        ArrayList<LogEntry> list = new Gson().fromJson(json, new TypeToken<ArrayList<LogEntry>>(){}.getType());

        model.addAttribute("logs",list);


        return "modelPages/logPage";
    }


    private String getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = "";

        if (authentication != null && authentication.isAuthenticated()) {
            userRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }

        return userRole;
    }
}
