package com.belairline.repository;

import com.belairline.model.Booking;
import com.belairline.model.BookingAgent;
import com.belairline.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    // B2 - DERIVED: enum parameter + OrderBy
    List<Booking> findByStatusOrderByTravelDateAsc(BookingStatus status);

    // B3 - DERIVED: Between (both dates inclusive)
    List<Booking> findByTravelDateBetweenOrderByTravelDateAsc(LocalDate start, LocalDate end);

    // D1 - DERIVED: existsBy + And + Not
    boolean existsByAgentAndTravelDateAndStatusNot(BookingAgent agent, LocalDate travelDate, BookingStatus status);

    // F1 - JPQL GROUP BY + COUNT
    @Query("SELECT b.status, COUNT(b) FROM Booking b GROUP BY b.status")
    List<Object[]> countBookingsByStatus();

    // F2 - JPQL UPDATE: one query, no Java loop
    @Modifying
    @Query("UPDATE Booking b SET b.status = :cancelled " +
            "WHERE b.agent.id = :bookingAgentId AND b.travelDate = :date " +
            "AND b.status <> :ticketed AND b.status <> :cancelled")
    int cancelDay(@Param("bookingAgentId") UUID bookingAgentId,
                  @Param("date") LocalDate date,
                  @Param("cancelled") BookingStatus cancelled,
                  @Param("ticketed") BookingStatus ticketed);

    // Bonus dashboard - DERIVED countBy
    long countByTravelDate(LocalDate travelDate);

    long countByStatus(BookingStatus status);
}
