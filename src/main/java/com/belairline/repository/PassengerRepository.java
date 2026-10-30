package com.belairline.repository;

import com.belairline.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PassengerRepository extends JpaRepository<Passenger, UUID> {

    // B1 - DERIVED: IgnoreCase + OrderBy
    List<Passenger> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // C4 - JPQL DISTINCT + path navigation (b.agent.id)
    @Query("SELECT DISTINCT b.passenger FROM Booking b WHERE b.agent.id = :bookingAgentId")
    List<Passenger> findPassengersOfBookingAgent(@Param("bookingAgentId") UUID bookingAgentId);
}
