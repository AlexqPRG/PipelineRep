package com.web_project.zayavki.controllers.modelControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.web_project.zayavki.models.SpecializationModel;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/specialization")
public class SpecializationController {
    @Autowired
    private ApiService apiService;

    private final String url = "/specialization";

    @GetMapping("/all")
    public String getSpecialization(Model model){
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url);

        ArrayList<SpecializationModel> list = new Gson().fromJson(json, new TypeToken<ArrayList<SpecializationModel>>(){}.getType());

        model.addAttribute("specializations",list);

        model.addAttribute("specialization", new SpecializationModel());

        return "modelPages/specializationPage";
    }

    @GetMapping("/all/{id}")
    public String getSpecializationById(@PathVariable("id") UUID id, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url + "/" +  id);
        SpecializationModel specialization = new ObjectMapper().readValue(json, SpecializationModel.class);
        model.addAttribute("specializations", specialization);
        model.addAttribute("specialization", new SpecializationModel());
        return "modelPages/specializationPage";
    }

    @PostMapping("/add")
    public String createSpecialization(@Valid @ModelAttribute("specialization") SpecializationModel specializationModel, BindingResult result, Model model){
        model.addAttribute("userRole", getRole());
        String json = apiService.setDataToApi(url, specializationModel);
        return "redirect:/specialization/all";
    }

    @PostMapping("/update")
    public String updateSpecialization(@Valid @ModelAttribute("specialization") SpecializationModel specializationModel, BindingResult result) {
//        clientModel.setOrder(orderList);
        String json = apiService.updateDataWithApi(url + "/" +  specializationModel.getId(), specializationModel);
        return "redirect:/specialization/all";
    }

    @PostMapping("/delete")
    public String deleteSpecialization(@RequestParam UUID id){
        String json = apiService.deleteDataWithApi(url + "/" +  id);
        return "redirect:/specialization/all";
    }

    @PostMapping("/search")
    public String searchSpecialization(@RequestParam("query") String query, Model model){
        model.addAttribute("userRole", getRole());

        String json = apiService.getDataFromApi(url);
        ArrayList<SpecializationModel> list = new Gson().fromJson(json, new TypeToken<ArrayList<SpecializationModel>>(){}.getType());

        String lowerCaseQuery = query.toLowerCase();
        List<SpecializationModel> filteredList = list.stream()
                .filter(specializationModel ->
                            specializationModel.getName() != null &&
                            specializationModel.getName().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        model.addAttribute("specializations", filteredList);
        model.addAttribute("specialization", new SpecializationModel());

        return "modelPages/specializationPage";
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
