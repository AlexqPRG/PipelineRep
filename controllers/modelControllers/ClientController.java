package com.web_project.zayavki.controllers.modelControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.web_project.zayavki.models.ClientModel;
import com.web_project.zayavki.models.RoleEnum;
import com.web_project.zayavki.models.UserModel;
import com.web_project.zayavki.service.ApiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

@Controller
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ApiService apiService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String url = "/client";

    @GetMapping("/all")
    public String getClient(Model model){
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url);

        ArrayList<ClientModel> list = new Gson().fromJson(json, new TypeToken<ArrayList<ClientModel>>(){}.getType());

        model.addAttribute("clients",list);

        model.addAttribute("user", new UserModel());

//        json = apiService.getDataFromApi("/user/without");
//        ArrayList<UserModel> list_users = new Gson().fromJson(json, new TypeToken<ArrayList<UserModel>>(){}.getType());
//
//        model.addAttribute("users", list_users);
        return "modelPages/clientPage";
    }

    @GetMapping("/all/{id}")
    public String getClientById(@PathVariable("id") UUID id, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url + "/" +  id);

        ClientModel client = new ObjectMapper().readValue(json, ClientModel.class);
        model.addAttribute("clients", client);
        model.addAttribute("client", new ClientModel());
        return "modelPages/clientPage";
    }

    @PostMapping("/add")
    public String createClient(@Valid @ModelAttribute("user") UserModel userModel, Model model){
        model.addAttribute("userRole", getRole());
        userModel.setClient(new ClientModel());
        userModel.setRoles(Collections.singleton(RoleEnum.USER));
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        String json = apiService.setDataToApi("/user", userModel);
        return "redirect:/client/all";
    }

    @PostMapping("/update")
    public String updateClient(@Valid @ModelAttribute("user") UserModel userModel, @RequestParam("user_id") UUID user_id) throws JsonProcessingException {
        userModel.setRoles(Collections.singleton(RoleEnum.USER));
        String json = apiService.updateDataWithApi("/user" + "/" +  userModel.getId(), userModel);
        return "redirect:/client/all";
    }

    @PostMapping("/delete")
    public String deleteClient(@RequestParam UUID id){
        String json = apiService.deleteDataWithApi("/user" + "/" +  id);
        return "redirect:/client/all";
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
