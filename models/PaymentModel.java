package com.web_project.zayavki.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class PaymentModel {
    @Id
    @GeneratedValue
    private UUID id;
    private double sum;
    private String date;
    private String status;
    private boolean isExist = true;

    //связь с таблицей клиенты
    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private ClientModel client;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "work_id")
    private WorkModel work;


    public PaymentModel(){}

    public PaymentModel(UUID id, double sum, String date, String status, boolean isExist, ClientModel client, WorkModel work) {
        this.id = id;
        this.sum = sum;
        this.date = date;
        this.status = status;
        this.isExist = isExist;
        this.client = client;
        this.work = work;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
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

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public WorkModel getWork() {
        return work;
    }

    public void setWork(WorkModel work) {
        this.work = work;
    }
}

