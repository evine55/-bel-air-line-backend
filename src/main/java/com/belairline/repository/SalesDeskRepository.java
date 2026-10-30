package com.belairline.repository;

import com.belairline.model.SalesDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalesDeskRepository extends JpaRepository<SalesDesk, UUID> {

    // C2 - JPQL NOT EXISTS subquery: desks no booking agent sits at
    @Query("SELECT l FROM SalesDesk l WHERE NOT EXISTS " +
            "(SELECT p FROM BookingAgent p WHERE p.salesDesk = l) ORDER BY l.deskNumber")
    List<SalesDesk> findFreeSalesDesks();

    // D4 - DERIVED
    Optional<SalesDesk> findByDeskNumber(int deskNumber);
}
