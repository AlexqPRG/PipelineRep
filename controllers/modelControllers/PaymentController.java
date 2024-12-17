package com.web_project.zayavki.controllers.modelControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.web_project.zayavki.controllers.PayKassaController;
import com.web_project.zayavki.models.ClientModel;
import com.web_project.zayavki.models.PaymentModel;
//import com.web_project.zayavki.models.WorkModel;
import com.web_project.zayavki.models.PaymentRequest;
import com.web_project.zayavki.models.WorkModel;
import com.web_project.zayavki.service.ApiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private ApiService apiService;

    private final String url = "/payment";

    private final String shopId = "493489";
    private final String secretKey = "test_ibpCN0Z2kDHl7upBDwvzJzzg_-0HDaPAdTERZ-nwLRg";
    private final String apiUrl = "https://api.yookassa.ru/v3/payments";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PaymentController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/successPay/{id}")
    public String successPay(@PathVariable("id") UUID id) throws JsonProcessingException {
        String json = apiService.getDataFromApi("/payment/succesPayment" + "/" +  id);
//        PaymentModel payment = new ObjectMapper().readValue(json, PaymentModel.class);
//        payment.setStatus("Оплачено");
//        apiService.updateDataWithApi(url + "/" +  payment.getId(), payment);
        return "redirect:/payment/all";
    }

    @GetMapping("/all")
    public String getPayment(Model model, @RequestParam(required = false) UUID clientId, @RequestParam(required = false, defaultValue = "asc") String sort){
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url);

        List<PaymentModel> list = new Gson().fromJson(json, new TypeToken<List<PaymentModel>>(){}.getType());

        ArrayList<PaymentModel> filtered_list = new ArrayList<>();
        if(getRole().contains("USER")){
            String username = getUser();
            String jsonClient = apiService.getDataFromApi("/client");
            ArrayList<ClientModel> list1 = new Gson().fromJson(jsonClient, new TypeToken<ArrayList<ClientModel>>(){}.getType());
            Optional<ClientModel> client = list1.stream().filter(object -> object.getUser().getUsername().equals(username)).findFirst();
            List<PaymentModel> finalList = list;
            client.ifPresent(clientModel ->{
                for(var item: finalList){
                    if(item.getClient().getId().equals(clientModel.getId())){
                        filtered_list.add(item);
                    }
                }
            });
        }
        if(filtered_list.size() != 0){
            list = filtered_list;
        }


        if(clientId != null){
            list = list.stream().filter(paymentModel -> paymentModel.getClient().getId().equals(clientId)).collect(Collectors.toList());
        }

        if("desc".equals(sort)){
            list.sort(Comparator.comparing(PaymentModel::getSum).reversed());
        }else{
            list.sort(Comparator.comparing(PaymentModel::getSum));
        }

        model.addAttribute("payments",list);

        model.addAttribute("payment", new PaymentModel());

        json = apiService.getDataFromApi("/client");
        ArrayList<ClientModel> list_clients = new Gson().fromJson(json, new TypeToken<ArrayList<ClientModel>>(){}.getType());

        model.addAttribute("clients", list_clients);

        json = apiService.getDataFromApi("/work");
        ArrayList<WorkModel> list_works = new Gson().fromJson(json, new TypeToken<ArrayList<WorkModel>>(){}.getType());

        model.addAttribute("works", list_works);

        return "modelPages/paymentPage";
    }

    @GetMapping("/all/{id}")
    public String getPaymentById(@PathVariable("id") UUID id, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        String json = apiService.getDataFromApi(url + "/" +  id);
        PaymentModel payment = new ObjectMapper().readValue(json, PaymentModel.class);
        model.addAttribute("payments", payment);
        model.addAttribute("payment", new PaymentModel());
        return "modelPages/paymentPage";
    }

    @PostMapping("/add")
    public String createPayment(@ModelAttribute("payment") PaymentModel paymentModel, Model model) throws JsonProcessingException {
        model.addAttribute("userRole", getRole());
        String json = apiService.setDataToApi(url, paymentModel);
        return "redirect:/payment/all";
    }

    @PostMapping("/update")
    public String updatePayment(@ModelAttribute("payment") PaymentModel paymentModel) {
        String json = apiService.updateDataWithApi(url + "/" +  paymentModel.getId(), paymentModel);
        return "redirect:/payment/all";
    }

    @PostMapping("/delete")
    public String deletePayment(@RequestParam UUID id){
        String json = apiService.deleteDataWithApi(url + "/" +  id);
        return "redirect:/payment/all";
    }

    @GetMapping("/pay/{id}")
    public String createPayment(@PathVariable("id") UUID id, Model model) throws JsonProcessingException {
        String json = apiService.getDataFromApi("/payment/getById" + "/" + id);
        Double sum = Double.valueOf(json);
//        PaymentModel paymentModel = new ObjectMapper().readValue(json, PaymentModel.class);

        PaymentRequest paymentRequest = new PaymentRequest();

        paymentRequest.setAmount(new PaymentRequest.Amount(String.valueOf(sum), "RUB"));
        paymentRequest.setConfirmation(new PaymentRequest.Confirmation("embedded"));
        paymentRequest.setCapture(true);

        ResponseEntity<String> response = createPayment(paymentRequest);

        if(response.getStatusCode() == HttpStatus.OK){
            String confirmationToken = response.getBody();
            model.addAttribute("confirmationToken", confirmationToken);
            model.addAttribute("payId", id);
            return "modelPages/pay";
        }else{
            return "modelPages/main";
        }
    }

    public ResponseEntity<String> createPayment(@RequestBody PaymentRequest paymentRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((shopId + ":" + secretKey).getBytes()));
        headers.set("Idempotence-Key", UUID.randomUUID().toString());

        try {
            JsonNode requestBody = objectMapper.valueToTree(paymentRequest);
            HttpEntity<JsonNode> requestEntity = new HttpEntity<>(requestBody, headers);
            JsonNode response = restTemplate.postForObject(apiUrl, requestEntity, JsonNode.class);
            return ResponseEntity.ok(response.path("confirmation").path("confirmation_token").asText());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating payment");
        }
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
