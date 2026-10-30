package com.belairline.repository;

import com.belairline.model.AncillaryPurchase;
import com.belairline.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AncillaryPurchaseRepository extends JpaRepository<AncillaryPurchase, UUID> {

    // E2 - JPQL path navigation + ORDER BY (newest first)
    @Query("SELECT d FROM AncillaryPurchase d WHERE d.booking.passenger.passportNumber = :passportNumber " +
            "ORDER BY d.purchaseDate DESC")
    List<AncillaryPurchase> findHistoryOfPassenger(@Param("passportNumber") String passportNumber);

    // E3 - DERIVED
    boolean existsByBooking(Booking booking);
}
