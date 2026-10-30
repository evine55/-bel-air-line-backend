package com.belairline.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "booking_agent")
public class BookingAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;

    // many booking agents -> one sales desk (nullable)
    @ManyToOne
    @JoinColumn(name = "sales_desk_id")
    private SalesDesk salesDesk;

    // owning side of the many-to-many: this entity writes the join table
    @ManyToMany
    @JoinTable(name = "booking_agent_route",
            joinColumns = @JoinColumn(name = "booking_agent_id"),
            inverseJoinColumns = @JoinColumn(name = "route_id"))
    private List<Route> routes = new ArrayList<>();

    public BookingAgent() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public SalesDesk getSalesDesk() { return salesDesk; }
    public void setSalesDesk(SalesDesk salesDesk) { this.salesDesk = salesDesk; }
    public List<Route> getRoutes() { return routes; }
    public void setRoutes(List<Route> routes) { this.routes = routes; }
}
