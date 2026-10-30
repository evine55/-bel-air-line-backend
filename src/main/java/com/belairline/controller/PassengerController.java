package com.belairline.controller;

import com.belairline.model.Passenger;
import com.belairline.service.BookingAgentService;
import com.belairline.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private BookingAgentService bookingAgentService;

    @PostMapping("/save")
    public ResponseEntity<String> savePassenger(@RequestBody Passenger passenger) {
        String message = passengerService.savePassenger(passenger);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        return new ResponseEntity<>(passengerService.getAllPassengers(), HttpStatus.OK);
    }

    // B1
    @GetMapping("/by-last-name")
    public ResponseEntity<List<Passenger>> getByLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(passengerService.getPassengersByLastName(lastName), HttpStatus.OK);
    }

    // C4
    @GetMapping("/of-booking-agent/{bookingAgentId}")
    public ResponseEntity<?> getPassengersOfBookingAgent(@PathVariable UUID bookingAgentId) {
        if (bookingAgentService.getBookingAgentById(bookingAgentId).isEmpty()) {
            return new ResponseEntity<>("The booking agent with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(passengerService.getPassengersOfBookingAgent(bookingAgentId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPassengerById(@PathVariable UUID id) {
        Optional<Passenger> passenger = passengerService.getPassengerById(id);
        if (passenger.isPresent()) {
            return new ResponseEntity<>(passenger.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The passenger with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePassenger(@PathVariable UUID id, @RequestBody Passenger passenger) {
        String message = passengerService.updatePassenger(id, passenger);
        if (message.equals("The passenger with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePassenger(@PathVariable UUID id) {
        String message = passengerService.deletePassenger(id);
        if (message.equals("The passenger with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
