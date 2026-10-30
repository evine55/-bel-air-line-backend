package com.belairline.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "sales_desk")
public class SalesDesk {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int deskNumber;

    private String name;

    public SalesDesk() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public int getDeskNumber() { return deskNumber; }
    public void setDeskNumber(int deskNumber) { this.deskNumber = deskNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
