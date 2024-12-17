package com.web_project.zayavki.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.UUID;

@Entity
public class PassportModel {
    @Id
    @GeneratedValue
    private UUID id;
    private int series; //серия
    private int number; //номер
    private String date_of_issue; //дата выдачи
    private String name_of_subdivision; //имя выдавшего органа
    private String code_of_subdivision; //код подразделения
    private boolean isExist = true;

    //связь паспорта с таблицей сотрудники
    @OneToOne(optional = false, mappedBy = "passport")
    private StaffModel owner;

    public PassportModel(){}

    public PassportModel(UUID id, int series, int number, String date_of_issue, String name_of_subdivision, String code_of_subdivision, boolean isExist, StaffModel owner) {
        this.id = id;
        this.series = series;
        this.number = number;
        this.date_of_issue = date_of_issue;
        this.name_of_subdivision = name_of_subdivision;
        this.code_of_subdivision = code_of_subdivision;
        this.isExist = isExist;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDate_of_issue() {
        return date_of_issue;
    }

    public void setDate_of_issue(String date_of_issue) {
        this.date_of_issue = date_of_issue;
    }

    public String getName_of_subdivision() {
        return name_of_subdivision;
    }

    public void setName_of_subdivision(String name_of_subdivision) {
        this.name_of_subdivision = name_of_subdivision;
    }

    public String getCode_of_subdivision() {
        return code_of_subdivision;
    }

    public void setCode_of_subdivision(String code_of_subdivision) {
        this.code_of_subdivision = code_of_subdivision;
    }

    public StaffModel getOwner() {
        return owner;
    }

    public void setOwner(StaffModel owner) {
        this.owner = owner;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}
