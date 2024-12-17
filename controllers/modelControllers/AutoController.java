package com.web_project.zayavki.controllers.modelControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.web_project.zayavki.models.AutoModel;
import com.web_project.zayavki.models.ClientModel;
import com.web_project.zayavki.models.RequestModel;
import com.web_project.zayavki.service.ApiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auto")
public class AutoController {
    @Autowired
    private ApiService apiService;

    private final String url = "/auto";

    @GetMapping("/all")
    public String getAuto(Model model, @RequestParam(required = false, defaultValue = "asc") String sort){

        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url);
        ArrayList<AutoModel> list = new Gson().fromJson(json, new TypeToken<ArrayList<AutoModel>>(){}.getType());
        if("desc".equals(sort)){
            list.sort(Comparator.comparing(AutoModel::getDateOfRelease).reversed());
        }else{
            list.sort(Comparator.comparing(AutoModel::getDateOfRelease));
        }

        if(getRole().contains("USER")){
            String username = getUser();
            String jsonClient = apiService.getDataFromApi("/client");
            ArrayList<ClientModel> list1 = new Gson().fromJson(jsonClient, new TypeToken<ArrayList<ClientModel>>(){}.getType());
            Optional<ClientModel> client = list1.stream().filter(object -> object.getUser().getUsername().equals(username)).findFirst();
            client.ifPresent(findClient -> {
                model.addAttribute("client_idValue",findClient.getId());
                ArrayList<AutoModel> filteredList = new ArrayList<>();
                for(var item : list){
                    if(item.getClient().getId().equals(findClient.getId())){
                        filteredList.add(item);
                    }
                }
                model.addAttribute("autos",filteredList);
            });


        }else{
            model.addAttribute("autos",list);
        }

        model.addAttribute("auto", new AutoModel());

        json= apiService.getDataFromApi("/client");
        ArrayList<ClientModel> client_list = new Gson().fromJson(json, new TypeToken<ArrayList<ClientModel>>(){}.getType());

        model.addAttribute("clients", client_list);

        return "modelPages/autoPage";
    }

    @GetMapping("/all/{id}")
    public String getAutoById(@PathVariable("id") UUID id, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url + "/" +  id);
        AutoModel auto = new ObjectMapper().readValue(json, AutoModel.class);
        model.addAttribute("autos", auto);
        model.addAttribute("auto", new AutoModel());

        json= apiService.getDataFromApi("/client");
        ArrayList<ClientModel> client_list = new Gson().fromJson(json, new TypeToken<ArrayList<ClientModel>>(){}.getType());

        model.addAttribute("clients", client_list);
        return "modelPages/autoPage";
    }

    @PostMapping("/add")
    public String createAuto(@Valid @ModelAttribute("auto") AutoModel autoModel, Model model){
        model.addAttribute("userRole", getRole());
        String json = apiService.setDataToApi(url, autoModel);
        return "redirect:/auto/all";
    }

    @PostMapping("/update")
    public String updateAuto(@Valid @ModelAttribute("auto") AutoModel autoModel, BindingResult result) {
        String json = apiService.updateDataWithApi(url + "/" +  autoModel.getId(), autoModel);
        return "redirect:/auto/all";
    }

    @PostMapping("/delete")
    public String deleteAuto(@RequestParam UUID id){
        String json = apiService.deleteDataWithApi(url + "/" +  id);
        return "redirect:/auto/all";
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

    private String getUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object user = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) user;
        return ((UserDetails) user).getUsername();
    }
}
