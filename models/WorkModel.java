package com.web_project.zayavki.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class WorkModel {
    @Id
    @GeneratedValue
    private UUID id;
    private String dateOfWork;
    private String whatDo;
    private boolean isExist = true;


    //связь с таблицей сотрудники
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffModel staff;

    @ManyToOne
    @JoinColumn(name = "auto_id")
    private AutoModel auto;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RequestModel request;


    @OneToOne(optional = true, mappedBy = "work")
    private PaymentModel payment;


    @OneToMany(mappedBy = "work")
    private List<SpareWork> spareWorks;

    public WorkModel(){}

    public WorkModel(UUID id, String dateOfWork, String whatDo, boolean isExist, StaffModel staff, AutoModel auto, RequestModel request, PaymentModel payment, List<SpareWork> spareWorks) {
        this.id = id;
        this.dateOfWork = dateOfWork;
        this.whatDo = whatDo;
        this.isExist = isExist;
        this.staff = staff;
        this.auto = auto;
        this.request = request;
        this.payment = payment;
        this.spareWorks = spareWorks;
    }



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDateOfWork() {
        return dateOfWork;
    }

    public void setDateOfWork(String dateOfWork) {
        this.dateOfWork = dateOfWork;
    }

    public String getWhatDo() {
        return whatDo;
    }

    public void setWhatDo(String whatDo) {
        this.whatDo = whatDo;
    }

    public StaffModel getStaff() {
        return staff;
    }

    public void setStaff(StaffModel staff) {
        this.staff = staff;
    }

    public AutoModel getAuto() {
        return auto;
    }

    public void setAuto(AutoModel auto) {
        this.auto = auto;
    }

    public RequestModel getRequest() {
        return request;
    }

    public void setRequest(RequestModel request) {
        this.request = request;
    }

    public List<SpareWork> getSpareWorks() {
        return spareWorks;
    }

    public void setSpareWorks(List<SpareWork> spareWorks) {
        this.spareWorks = spareWorks;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public PaymentModel getPayment() {
        return payment;
    }

    public void setPayment(PaymentModel payment) {
        this.payment = payment;
    }
}


