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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private ApiService apiService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String url = "/staff";

    @GetMapping("/all")
    public String getStaff(Model model){
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url);

        ArrayList<StaffModel> list = new Gson().fromJson(json, new TypeToken<ArrayList<StaffModel>>(){}.getType());

        model.addAttribute("staffs",list);

//        StaffModel staffModel = new StaffModel();
//        staffModel.setSpecializationModelList(new ArrayList<>());
//        model.addAttribute("staff", staffModel);

        UserModel userModel = new UserModel();
        userModel.setStaff(new StaffModel());
        model.addAttribute("user", userModel);

//        json = apiService.getDataFromApi("/user");
//        ArrayList<UserModel> list_users = new Gson().fromJson(json, new TypeToken<ArrayList<UserModel>>(){}.getType());
//
//        model.addAttribute("users", list_users);

        json = apiService.getDataFromApi("/specialization");
        ArrayList<SpecializationModel> specializationModels = new Gson().fromJson(json, new TypeToken<ArrayList<SpecializationModel>>(){}.getType());

        model.addAttribute("specializations", specializationModels);

        List<UUID> specializationIds = new ArrayList<>();
        for(var item: list){
            specializationIds = item.getSpecializationModelList().stream().map(SpecializationModel::getId).collect(Collectors.toList());
        }

        model.addAttribute("staffSpecIds", specializationIds);

        return "modelPages/staffPage";
    }

    @GetMapping("/all/{id}")
    public String getStaffById(@PathVariable("id") UUID id, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url + "/" +  id);
        StaffModel staff = new ObjectMapper().readValue(json, StaffModel.class);
        model.addAttribute("staffs", staff);
        UserModel userModel = new UserModel();
        userModel.setStaff(new StaffModel());
        model.addAttribute("user", userModel);
        json = apiService.getDataFromApi("/specialization");
        ArrayList<SpecializationModel> specializationModels = new Gson().fromJson(json, new TypeToken<ArrayList<SpecializationModel>>(){}.getType());

        model.addAttribute("specializations", specializationModels);

        List<UUID> specializationIds = new ArrayList<>();
        for(var item: staff.getSpecializationModelList()){
            specializationIds.add(item.getId()) ;
        }

        model.addAttribute("staffSpecIds", specializationIds);
        return "modelPages/staffPage";
    }

    @PostMapping("/add")
    public String createStaff(@ModelAttribute("user") UserModel userModel,@RequestParam("specializationIds") List<UUID> listIds, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        List<SpecializationModel> specializationModels = new ArrayList<>();
        for(var item: listIds){
            String spec_json = apiService.getDataFromApi("/specialization/" + item);
            SpecializationModel specializationModel = new ObjectMapper().readValue(spec_json, SpecializationModel.class);
            specializationModels.add(specializationModel);
        }
        userModel.getStaff().setSpecializationModelList(specializationModels);
        userModel.setRoles(Collections.singleton(RoleEnum.STAFF));
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        String json = apiService.setDataToApi("/user", userModel);
        return "redirect:/staff/all";
    }

    @PostMapping("/update")
    public String updateStaff(@Valid @ModelAttribute("user") UserModel userModel, @RequestParam("specializationIds") List<UUID> listIds) throws JsonProcessingException {

        List<SpecializationModel> specializationModels = new ArrayList<>();
        for(var item: listIds){
            String spec_json = apiService.getDataFromApi("/specialization/" + item);
            SpecializationModel specializationModel = new ObjectMapper().readValue(spec_json, SpecializationModel.class);
            specializationModels.add(specializationModel);
        }
        String json_user = apiService.getDataFromApi("/staff/" + userModel.getStaff().getId());
        StaffModel staff = new ObjectMapper().readValue(json_user, StaffModel.class);

        staff.setPassport(userModel.getStaff().getPassport());
        staff.setSpecializationModelList(specializationModels);
        staff.setDogovorModelList(userModel.getStaff().getDogovorModelList());
        staff.setWorkModelList(userModel.getStaff().getWorkModelList());
        UserModel userModel1 = new UserModel();
        userModel1.setId(staff.getUser().getId());
        staff.setUser(userModel1);

        apiService.updateDataWithApi("/staff" + "/" +  staff.getId(), staff);

        userModel.setStaff(null);
        userModel.setRoles(Collections.singleton(RoleEnum.STAFF));

        String json = apiService.updateDataWithApi("/user" + "/" +  userModel.getId(), userModel);
        return "redirect:/staff/all";
    }

    @PostMapping("/delete")
    public String deleteStaff(@RequestParam UUID id){
        String json = apiService.deleteDataWithApi("/user" + "/" +  id);
        return "redirect:/staff/all";
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
