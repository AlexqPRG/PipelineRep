package com.web_project.zayavki.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class SpecializationModel {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private boolean isExist = true;

    //связь многие ко многим с таблицей сотрудники
    @ManyToMany
    @JoinTable(name = "staff_specialization",
            joinColumns = @JoinColumn(name = "specialization_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<StaffModel> staffModelList;

    public SpecializationModel(){}

    public SpecializationModel(UUID id, String name, boolean isExist, List<StaffModel> staffModelList) {
        this.id = id;
        this.name = name;
        this.isExist = isExist;
        this.staffModelList = staffModelList;
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

    public List<StaffModel> getStaffModelList() {
        return staffModelList;
    }

    public void setStaffModelList(List<StaffModel> staffModelList) {
        this.staffModelList = staffModelList;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}
