package com.web_project.zayavki.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class StaffModel {
    @Id
    @GeneratedValue
    private UUID id;
    private boolean isExist = true;



    //связь с паспортом
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private PassportModel passport;

    //связь с договором
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<DogovorModel> dogovorModelList;

    //связь с таблицей специализация
    @ManyToMany
    @JoinTable(name = "staff_specialization",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<SpecializationModel> specializationModelList;

    //связь с таблицей работы
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<WorkModel> workModelList;

    //связь с таблицей пользователи
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private UserModel user;

    public StaffModel(){}

    public StaffModel(UUID id, boolean isExist, PassportModel passport, List<DogovorModel> dogovorModelList, List<SpecializationModel> specializationModelList, List<WorkModel> workModelList, UserModel user) {
        this.id = id;
        this.isExist = isExist;
        this.passport = passport;
        this.dogovorModelList = dogovorModelList;
        this.specializationModelList = specializationModelList;
        this.workModelList = workModelList;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PassportModel getPassport() {
        return passport;
    }

    public void setPassport(PassportModel passport) {
        this.passport = passport;
    }

    public List<DogovorModel> getDogovorModelList() {
        return dogovorModelList;
    }

    public void setDogovorModelList(List<DogovorModel> dogovorModelList) {
        this.dogovorModelList = dogovorModelList;
    }

    public List<SpecializationModel> getSpecializationModelList() {
        return specializationModelList;
    }

    public void setSpecializationModelList(List<SpecializationModel> specializationModelList) {
        this.specializationModelList = specializationModelList;
    }

    public List<WorkModel> getWorkModelList() {
        return workModelList;
    }

    public void setWorkModelList(List<WorkModel> workModelList) {
        this.workModelList = workModelList;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}
