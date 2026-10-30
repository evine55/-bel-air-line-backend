package com.belairline.controller;

import com.belairline.model.Booking;
import com.belairline.model.BookingStatus;
import com.belairline.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // A2 + D1
    @PostMapping("/save")
    public ResponseEntity<String> saveBooking(@RequestBody Booking booking) {
        String message = bookingService.saveBooking(booking);
        if (message.equals("Booking date cannot be in the past")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if (message.equals("Agent already has a booking on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else if (message.equals("The passenger with that id does not exist")
                || message.equals("The booking agent with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }

    // B2 - Spring converts "RESERVED" into the enum
    @GetMapping("/by-status")
    public ResponseEntity<List<Booking>> getByStatus(@RequestParam BookingStatus status) {
        return new ResponseEntity<>(bookingService.getBookingsByStatus(status), HttpStatus.OK);
    }

    // B3 - parse the Strings into LocalDate here in the controller
    @GetMapping("/between")
    public ResponseEntity<List<Booking>> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return new ResponseEntity<>(bookingService.getBookingsBetween(startDate, endDate), HttpStatus.OK);
    }

    // F1
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getStatsByStatus() {
        return new ResponseEntity<>(bookingService.getStatsByStatus(), HttpStatus.OK);
    }

    // F2
    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID bookingAgentId, @RequestParam String date) {
        String message = bookingService.cancelDay(bookingAgentId, LocalDate.parse(date));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Bonus - build the Pageable from page, size and sort=field,direction
    @GetMapping("/page")
    public ResponseEntity<Page<Booking>> getPage(@RequestParam int page,
                                                 @RequestParam int size,
                                                 @RequestParam String sort) {
        String[] parts = sort.split(",");
        String field = parts[0];
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, field));
        return new ResponseEntity<>(bookingService.getBookingsPage(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable UUID id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            return new ResponseEntity<>(booking.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The booking with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable UUID id, @RequestBody Booking booking) {
        String message = bookingService.updateBooking(id, booking);
        if (message.endsWith("does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // D2
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable UUID id, @RequestParam BookingStatus status) {
        String message = bookingService.changeStatus(id, status);
        if (message.equals("The booking with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.startsWith("Cannot change status from")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // A2 + E3
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable UUID id) {
        String message = bookingService.deleteBooking(id);
        if (message.equals("The booking with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That booking has ancillary purchases, delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
