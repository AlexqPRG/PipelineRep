package com.web_project.zayavki.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class SpareWork {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "work_id")
    private WorkModel work;

    @ManyToOne
    @JoinColumn(name = "spare_id")
    private SpareModel spare;

    private int quantity;

    // Constructors, getters and setters

    public SpareWork() {}

    public SpareWork(UUID id, WorkModel work, SpareModel spare, int quantity) {
        this.id = id;
        this.work = work;
        this.spare = spare;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WorkModel getWork() {
        return work;
    }

    public void setWork(WorkModel work) {
        this.work = work;
    }

    public SpareModel getSpare() {
        return spare;
    }

    public void setSpare(SpareModel spare) {
        this.spare = spare;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
