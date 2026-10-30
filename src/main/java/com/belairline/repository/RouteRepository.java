package com.belairline.repository;

import com.belairline.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, UUID> {

    // C3 - JPQL IS EMPTY: routes whose agents collection is empty
    @Query("SELECT r FROM Route r WHERE r.agents IS EMPTY")
    List<Route> findUnusedRoutes();
}
