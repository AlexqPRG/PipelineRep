package com.web_project.zayavki.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web_project.zayavki.models.PaymentRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.UUID;

@Controller
@RequestMapping("/paymentsKassa")
public class PayKassaController {
    private final String shopId = "493489";
    private final String secretKey = "test_ibpCN0Z2kDHl7upBDwvzJzzg_-0HDaPAdTERZ-nwLRg";
    private final String apiUrl = "https://api.yookassa.ru/v3/payments";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PayKassaController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/page")
    public String getPaymentPage() {
        return "modelPages/pay";
    }

    @PostMapping("/create")
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
}

