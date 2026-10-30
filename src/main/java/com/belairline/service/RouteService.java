package com.belairline.service;

import com.belairline.model.Route;
import com.belairline.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public String saveRoute(Route route) {
        routeRepository.save(route);
        return "Route saved successfully";
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> getRouteById(UUID id) {
        return routeRepository.findById(id);
    }

    public String updateRoute(UUID id, Route newRoute) {
        Optional<Route> existing = routeRepository.findById(id);
        if (existing.isEmpty()) {
            return "The route with that id does not exist";
        }
        Route route = existing.get();
        route.setName(newRoute.getName());
        routeRepository.save(route);
        return "Route updated successfully";
    }

    public String deleteRoute(UUID id) {
        if (!routeRepository.existsById(id)) {
            return "The route with that id does not exist";
        }
        routeRepository.deleteById(id);
        return "Route deleted successfully";
    }

    // C3
    public List<Route> getUnusedRoutes() {
        return routeRepository.findUnusedRoutes();
    }
}
