package com.web_project.zayavki.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class SpareModel {
    @Id
    @GeneratedValue
    private UUID id;
    private String article;
    private double price;
    private int quantity;
    private boolean isExist = true;

    @OneToMany(mappedBy = "spare")
    private List<SpareWork> spareWorks;

    public SpareModel(){}

    public SpareModel(UUID id, String article, double price, int quantity, boolean isExist, List<SpareWork> spareWorks) {
        this.id = id;
        this.article = article;
        this.price = price;
        this.quantity = quantity;
        this.isExist = isExist;
        this.spareWorks = spareWorks;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
