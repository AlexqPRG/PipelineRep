package com.web_project.zayavki.models;


import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class StatusModel {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private List<RequestModel> requestModels;

    public StatusModel(){}

    public StatusModel(UUID id, String name, List<RequestModel> requestModels) {
        this.id = id;
        this.name = name;
        this.requestModels = requestModels;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RequestModel> getRequestModels() {
        return requestModels;
    }

    public void setRequestModels(List<RequestModel> requestModels) {
        this.requestModels = requestModels;
    }
}
