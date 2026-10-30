package com.belairline.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ancillary_purchase")
public class AncillaryPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String itemName;
    private String note;
    private int quantity;
    private LocalDate purchaseDate;

    // many purchases -> one booking. Booking has NO list back (paper rule)
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public AncillaryPurchase() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}
