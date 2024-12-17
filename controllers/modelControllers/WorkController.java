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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/work")
public class WorkController {
    @Autowired
    private ApiService apiService;

    private final String url = "/work";

    @GetMapping("/all")
    public String getRequest(Model model){
        model.addAttribute("userRole", getRole());

        String json = apiService.getDataFromApi(url);

        ArrayList<WorkModel> list = new Gson().fromJson(json, new TypeToken<ArrayList<WorkModel>>(){}.getType());

        if(getRole().contains("STAFF")){
            String username = getUser();
            String json_staff = apiService.getDataFromApi("/staff");
            ArrayList<StaffModel> list1 = new Gson().fromJson(json_staff, new TypeToken<ArrayList<StaffModel>>(){}.getType());
            Optional<StaffModel> staff = list1.stream().filter(object -> object.getUser().getUsername().equals(username)).findFirst();
            staff.ifPresent(staffModel -> {
                model.addAttribute("staffID", staffModel.getId());
                ArrayList<WorkModel> filteredList = new ArrayList<>();
                for(var item: list){
                    if(item.getStaff().getId().equals(staffModel.getId())){
                        filteredList.add(item);
                    }
                }
                model.addAttribute("works", filteredList);
            });
        }else{
            model.addAttribute("works",list);
        }

        model.addAttribute("work", new WorkModel());

        json = apiService.getDataFromApi("/staff");

        ArrayList<StaffModel> list_staff = new Gson().fromJson(json, new TypeToken<ArrayList<StaffModel>>(){}.getType());

        model.addAttribute("staffs",list_staff);

//        json = apiService.getDataFromApi("/auto");

//        ArrayList<AutoModel> list_auto = new Gson().fromJson(json, new TypeToken<ArrayList<AutoModel>>(){}.getType());
//
//        model.addAttribute("autos",list_auto);

        json = apiService.getDataFromApi("/request");

        ArrayList<RequestModel> list_request = new Gson().fromJson(json, new TypeToken<ArrayList<RequestModel>>(){}.getType());

        model.addAttribute("requests",list_request);

        json = apiService.getDataFromApi("/spare");

        ArrayList<SpareModel> list_spare = new Gson().fromJson(json, new TypeToken<ArrayList<SpareModel>>(){}.getType());

        model.addAttribute("spares",list_spare);

        return "modelPages/workPage";
    }

    @GetMapping("/all/{id}")
    public String getWorkById(@PathVariable("id") UUID id, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url + "/" +  id);
        WorkModel work = new ObjectMapper().readValue(json, WorkModel.class);
        model.addAttribute("works", work);
        model.addAttribute("work", new WorkModel());
        return "modelPages/workPage";
    }

    @PostMapping("/add")
    public String createWork(@Valid @ModelAttribute("work") WorkModel workModel, Model model) throws JsonProcessingException {
        String json_request = apiService.getDataFromApi("/request/auto" + "/" +  workModel.getRequest().getId());
        AutoModel autoModel = new ObjectMapper().readValue(json_request, AutoModel.class);
        workModel.setAuto(autoModel);
        model.addAttribute("userRole", getRole());
        String json = apiService.setDataToApi(url, workModel);
        return "redirect:/work/all";
    }

    @PostMapping("/update")
    public String updateRequest(@Valid @ModelAttribute("work") WorkModel workModel, BindingResult result) {
        System.out.println(workModel.getAuto().getId());
        String json = apiService.updateDataWithApi(url + "/" +  workModel.getId(), workModel);
        return "redirect:/work/all";
    }

    @PostMapping("/delete")
    public String deleteWork(@RequestParam UUID id){
        String json = apiService.deleteDataWithApi(url + "/" +  id);
        return "redirect:/work/all";
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
