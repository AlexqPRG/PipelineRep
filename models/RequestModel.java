package com.web_project.zayavki.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class RequestModel {
    @Id
    @GeneratedValue
    private UUID id;
    private String description;
    private String date;
    private String status;
    private boolean isExist = true;


    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<WorkModel> workModelList;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private ClientModel client;

    @ManyToOne
    @JoinColumn(name = "auto_id")
    private AutoModel auto;

    public RequestModel(){}

    public RequestModel(UUID id, String description, String date, String status, boolean isExist, List<WorkModel> workModelList, ClientModel client, AutoModel auto) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.status = status;
        this.isExist = isExist;
        this.workModelList = workModelList;
        this.client = client;
        this.auto = auto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<WorkModel> getWorkModelList() {
        return workModelList;
    }

    public void setWorkModelList(List<WorkModel> workModelList) {
        this.workModelList = workModelList;
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public AutoModel getAuto() {
        return auto;
    }

    public void setAuto(AutoModel auto) {
        this.auto = auto;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}
