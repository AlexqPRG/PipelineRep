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
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private ApiService apiService;

    private final String url = "/request";

    @GetMapping("/all")
    public String getRequest(Model model, @RequestParam(required = false) UUID clientId){
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url);


        ArrayList<RequestModel> list = new Gson().fromJson(json, new TypeToken<ArrayList<RequestModel>>(){}.getType());



        if(getRole().contains("USER")){
            String username = getUser();
            String jsonClient = apiService.getDataFromApi("/client");
            ArrayList<ClientModel> list1 = new Gson().fromJson(jsonClient, new TypeToken<ArrayList<ClientModel>>(){}.getType());
            Optional<ClientModel> client = list1.stream().filter(object -> object.getUser().getUsername().equals(username)).findFirst();
            client.ifPresent(findClient -> {
                model.addAttribute("client_idValue",findClient.getId());
                ArrayList<RequestModel> filteredList = new ArrayList<>();
                for(var item : list){
                    if(item.getClient().getId().equals(findClient.getId())){
                        filteredList.add(item);
                    }
                }
                model.addAttribute("requests",filteredList);
            });


        }else{
            if(clientId != null){
                List<RequestModel> filteredList = new ArrayList<>();
                filteredList = list.stream().filter(requestModel -> requestModel.getClient().getId().equals(clientId)).toList();
                model.addAttribute("requests",filteredList);
            }else{
                model.addAttribute("requests",list);
            }

        }


        model.addAttribute("request", new RequestModel());

//        json = apiService.getDataFromApi("/status");
//
//        ArrayList<StatusModel> list_status = new Gson().fromJson(json, new TypeToken<ArrayList<StatusModel>>(){}.getType());
//
//        model.addAttribute("statuss",list_status);

        json = apiService.getDataFromApi("/client");

        ArrayList<ClientModel> list_client = new Gson().fromJson(json, new TypeToken<ArrayList<ClientModel>>(){}.getType());

        model.addAttribute("clients", list_client);



        return "modelPages/requestPage";
    }

    @GetMapping("/all/{id}")
    public String getRequestById(@PathVariable("id") UUID id, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url + "/" +  id);
        RequestModel request = new ObjectMapper().readValue(json, RequestModel.class);
        model.addAttribute("requests", request);
        model.addAttribute("request", new RequestModel());
        return "modelPages/requestPage";
    }

    @PostMapping("/add")
    public String createRequest(@ModelAttribute("request") RequestModel requestModel, @RequestParam("autoId") UUID autoId, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        if(getRole().contains("USER")){
            String json = apiService.getDataFromApi("/client");
            ArrayList<ClientModel> list = new Gson().fromJson(json, new TypeToken<ArrayList<ClientModel>>(){}.getType());
            String username = getUser();
            Optional<ClientModel> client = list.stream().filter(object -> object.getUser().getUsername().equals(username)).findFirst();

            client.ifPresent(findClient -> {
//                requestModel.setClient(findClient);
                requestModel.setStatus("В обработке");
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String formattedDate = currentDate.format(formatter);
                requestModel.setDate(formattedDate);
            });

        }

        String jsonAuto = apiService.getDataFromApi("/auto");
        ArrayList<AutoModel> list = new Gson().fromJson(jsonAuto, new TypeToken<ArrayList<AutoModel>>(){}.getType());
        for(var item: list){
            if(item.getId().equals(autoId)){
                item.setClient(null);
                item.setWorkModelList(new ArrayList<WorkModel>());
                requestModel.setAuto(item);
            }
        }
        //        String jsonClient = apiService.getDataFromApi("/client/" + requestModel.getClient().getId());
//        ClientModel clientModel = new ObjectMapper().readValue(jsonClient, ClientModel.class);
//        Optional<AutoModel> foundAuto = clientModel.getAutos().stream()
//                .filter(auto -> auto.getId().equals(autoId))
//                .findFirst();
//        foundAuto.ifPresent(findAuto -> {
//            findAuto.setClient(null);
//            requestModel.setAuto(findAuto);
//        });
        apiService.setDataToApi(url, requestModel);
        return "redirect:/request/all";
    }

    @PostMapping("/update")
    public String updateRequest(@Valid @ModelAttribute("request") RequestModel requestModel, BindingResult result) {
        String json = apiService.updateDataWithApi(url + "/" +  requestModel.getId(), requestModel);
        return "redirect:/request/all";
    }

    @PostMapping("/delete")
    public String deleteRequest(@RequestParam UUID id){
        String json = apiService.deleteDataWithApi(url + "/" +  id);
        return "redirect:/request/all";
    }


    @GetMapping("api/request")
    @ResponseBody
    public List<RequestModel> getRequestsApi() {
        String json = apiService.getDataFromApi(url);
        return new Gson().fromJson(json, new TypeToken<List<RequestModel>>(){}.getType());
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

    private String getUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object user = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) user;
        return ((UserDetails) user).getUsername();
    }
}
