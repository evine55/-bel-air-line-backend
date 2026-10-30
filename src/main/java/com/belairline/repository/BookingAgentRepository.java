package com.belairline.repository;

import com.belairline.model.BookingAgent;
import com.belairline.model.SalesDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookingAgentRepository extends JpaRepository<BookingAgent, UUID> {

    // C1 - JPQL JOIN on the many-to-many collection, LOWER for case-insensitive
    @Query("SELECT DISTINCT a FROM BookingAgent a JOIN a.routes r " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<BookingAgent> findByRouteName(@Param("name") String name);

    // D4 - DERIVED: is this sales desk already occupied?
    boolean existsBySalesDesk(SalesDesk salesDesk);
}
