package com.belairline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    // inverse side: the link is stored by BookingAgent; @JsonIgnore stops the JSON loop
    @ManyToMany(mappedBy = "routes")
    @JsonIgnore
    private List<BookingAgent> agents = new ArrayList<>();

    public Route() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<BookingAgent> getAgents() { return agents; }
    public void setAgents(List<BookingAgent> agents) { this.agents = agents; }
}
