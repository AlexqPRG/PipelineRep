package com.web_project.zayavki.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class ClientModel {
    @Id
    @GeneratedValue
    private UUID id;
    private boolean isExist = true;

    //связь с таблицей автомобили
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<AutoModel> autos;

    //связь с таблицей пользователи
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private UserModel user;

    //связь с таблицей платежи
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<PaymentModel> payments;

    //связь с таблицей заявки
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<RequestModel> requestModels;

    public ClientModel(){}

    public ClientModel(UUID id, boolean isExist, List<AutoModel> autos, UserModel user, List<PaymentModel> payments, List<RequestModel> requestModels) {
        this.id = id;
        this.isExist = isExist;
        this.autos = autos;
        this.user = user;
        this.payments = payments;
        this.requestModels = requestModels;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<AutoModel> getAutos() {
        return autos;
    }

    public void setAutos(List<AutoModel> autos) {
        this.autos = autos;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<PaymentModel> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentModel> payments) {
        this.payments = payments;
    }

    public List<RequestModel> getRequestModels() {
        return requestModels;
    }

    public void setRequestModels(List<RequestModel> requestModels) {
        this.requestModels = requestModels;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}
