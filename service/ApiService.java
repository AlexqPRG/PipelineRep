package com.web_project.zayavki.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiUrl = "http://localhost:7677/v1/api";


    public String getDataFromApi(String url){
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl+url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public <T> String setDataToApi(String url, T model){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String jsonBody = new Gson().toJson(model);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl+url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public <T> String updateDataWithApi(String url, T model){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String jsonBody = new Gson().toJson(model);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl+url, HttpMethod.PUT, entity, String.class);
        return response.getBody();
    }

    public String deleteDataWithApi(String url){
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl+url, HttpMethod.DELETE, entity, String.class);
        return response.getBody();
    }
}
