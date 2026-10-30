package com.belairline.controller;

import com.belairline.model.Route;
import com.belairline.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("/save")
    public ResponseEntity<String> saveRoute(@RequestBody Route route) {
        String message = routeService.saveRoute(route);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return new ResponseEntity<>(routeService.getAllRoutes(), HttpStatus.OK);
    }

    // C3
    @GetMapping("/unused")
    public ResponseEntity<List<Route>> getUnusedRoutes() {
        return new ResponseEntity<>(routeService.getUnusedRoutes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable UUID id) {
        Optional<Route> route = routeService.getRouteById(id);
        if (route.isPresent()) {
            return new ResponseEntity<>(route.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The route with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRoute(@PathVariable UUID id, @RequestBody Route route) {
        String message = routeService.updateRoute(id, route);
        if (message.equals("The route with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoute(@PathVariable UUID id) {
        String message = routeService.deleteRoute(id);
        if (message.equals("The route with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
