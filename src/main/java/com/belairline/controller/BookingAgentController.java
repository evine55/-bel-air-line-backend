package com.belairline.controller;

import com.belairline.model.BookingAgent;
import com.belairline.service.BookingAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking-agents")
public class BookingAgentController {

    @Autowired
    private BookingAgentService bookingAgentService;

    @PostMapping("/save")
    public ResponseEntity<String> saveBookingAgent(@RequestBody BookingAgent agent) {
        String message = bookingAgentService.saveBookingAgent(agent);
        if (message.equals("The sales desk with that id does not exist")
                || message.equals("That route does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookingAgent>> getAllBookingAgents() {
        return new ResponseEntity<>(bookingAgentService.getAllBookingAgents(), HttpStatus.OK);
    }

    // C1
    @GetMapping("/by-route")
    public ResponseEntity<List<BookingAgent>> getBookingAgentsByRoute(@RequestParam String name) {
        return new ResponseEntity<>(bookingAgentService.getBookingAgentsByRoute(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingAgentById(@PathVariable UUID id) {
        Optional<BookingAgent> agent = bookingAgentService.getBookingAgentById(id);
        if (agent.isPresent()) {
            return new ResponseEntity<>(agent.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The booking agent with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBookingAgent(@PathVariable UUID id, @RequestBody BookingAgent agent) {
        String message = bookingAgentService.updateBookingAgent(id, agent);
        if (message.equals("The booking agent with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBookingAgent(@PathVariable UUID id) {
        String message = bookingAgentService.deleteBookingAgent(id);
        if (message.equals("The booking agent with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // D3
    @PutMapping("/{bookingAgentId}/routes/{routeId}")
    public ResponseEntity<String> addRoute(@PathVariable UUID bookingAgentId, @PathVariable UUID routeId) {
        String message = bookingAgentService.addRouteToBookingAgent(bookingAgentId, routeId);
        if (message.equals("The booking agent with that id does not exist")
                || message.equals("That route does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("The booking agent already has that route")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // D4
    @PatchMapping("/{bookingAgentId}/sales-desk")
    public ResponseEntity<String> changeSalesDesk(@PathVariable UUID bookingAgentId, @RequestParam int deskNumber) {
        String message = bookingAgentService.changeSalesDesk(bookingAgentId, deskNumber);
        if (message.equals("The booking agent with that id does not exist")
                || message.equals("The sales desk with that deskNumber does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That sales desk is taken by another booking agent")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
