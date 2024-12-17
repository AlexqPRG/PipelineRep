package com.web_project.zayavki.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class DogovorModel {
    @Id
    @GeneratedValue
    private UUID id;
    private String functions; //трудовая функция
    private String dateOn; //дата принятия на работу
    private double salary; //оклад
    private String conditions; //условия работы
    private String operation_mode; //режим работы
    private boolean urgent; //срочный?
    private String dateOff; //дата окончания договора (необязательно)
    private boolean isExist = true;

    //связь с таблицей сотрудник
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffModel staff;

    public DogovorModel(){}

    public DogovorModel(UUID id, String functions, String dateOn, double salary, String conditions, String operation_mode, boolean urgent, String dateOff, boolean isExist, StaffModel staff) {
        this.id = id;
        this.functions = functions;
        this.dateOn = dateOn;
        this.salary = salary;
        this.conditions = conditions;
        this.operation_mode = operation_mode;
        this.urgent = urgent;
        this.dateOff = dateOff;
        this.isExist = isExist;
        this.staff = staff;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public String getDateOn() {
        return dateOn;
    }

    public void setDateOn(String dateOn) {
        this.dateOn = dateOn;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getOperation_mode() {
        return operation_mode;
    }

    public void setOperation_mode(String operation_mode) {
        this.operation_mode = operation_mode;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getDateOff() {
        return dateOff;
    }

    public void setDateOff(String dateOff) {
        this.dateOff = dateOff;
    }

    public StaffModel getStaff() {
        return staff;
    }

    public void setStaff(StaffModel staff) {
        this.staff = staff;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}
