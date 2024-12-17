package com.web_project.zayavki.controllers.modelControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.web_project.zayavki.models.*;
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
@RequestMapping("/spare")
public class SpareController {
    @Autowired
    private ApiService apiService;

    private final String url = "/spare";

    @GetMapping("/all")
    public String getSpare(Model model){
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url);

        ArrayList<SpareModel> list = new Gson().fromJson(json, new TypeToken<ArrayList<SpareModel>>(){}.getType());

        model.addAttribute("spares",list);

        model.addAttribute("spare", new SpareModel());

        return "modelPages/sparePage";
    }

    @GetMapping("/all/{id}")
    public String getSpareById(@PathVariable("id") UUID id, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url + "/" +  id);
        SpareModel spareModel = new ObjectMapper().readValue(json, SpareModel.class);
        model.addAttribute("spares", spareModel);
        model.addAttribute("spare", new SpareModel());
        return "modelPages/sparePage";
    }

    @PostMapping("/add")
    public String createSpare(@Valid @ModelAttribute("spare") SpareModel spareModel, Model model){
        model.addAttribute("userRole", getRole());
        String json = apiService.setDataToApi(url, spareModel);
        return "redirect:/spare/all";
    }

    @PostMapping("/update")
    public String updateSpare(@Valid @ModelAttribute("spare") SpareModel spareModel) {
        String json = apiService.updateDataWithApi(url + "/" +  spareModel.getId(), spareModel);
        return "redirect:/spare/all";
    }

    @PostMapping("/delete")
    public String deleteSpare(@RequestParam UUID id){
        String json = apiService.deleteDataWithApi(url + "/" +  id);
        return "redirect:/spare/all";
    }

    private String getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = "";

        if (authentication != null && authentication.isAuthenticated()) {
            // Получаем роли пользователя
            userRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }

        return userRole;
    }
}
