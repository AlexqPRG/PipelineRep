package com.web_project.zayavki.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@Entity
public class AutoModel {
    @Id
    @GeneratedValue
    private UUID id;
    private String brand; //марка
    private String model; //модель
    private String vin; //вин номер кузова
    private String dateOfRelease; //дата выпуска
    private boolean isExist = true;


    //связь с таблицей клиенты
    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private ClientModel client;

    @OneToMany(mappedBy = "auto", cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<WorkModel> workModelList;

    @OneToMany(mappedBy = "auto", cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<RequestModel> requestList;

    public AutoModel(){}

    public AutoModel(UUID id, String brand, String model, String vin, String dateOfRelease, boolean isExist, ClientModel client, List<WorkModel> workModelList, List<RequestModel> requestList) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.vin = vin;
        this.dateOfRelease = dateOfRelease;
        this.isExist = isExist;
        this.client = client;
        this.workModelList = workModelList;
        this.requestList = requestList;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(String dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public List<WorkModel> getWorkModelList() {
        return workModelList;
    }

    public void setWorkModelList(List<WorkModel> workModelList) {
        this.workModelList = workModelList;
    }

    public List<RequestModel> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestModel> requestList) {
        this.requestList = requestList;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}