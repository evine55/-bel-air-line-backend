package com.belairline.service;

import com.belairline.model.Booking;
import com.belairline.model.BookingAgent;
import com.belairline.model.BookingStatus;
import com.belairline.model.Passenger;
import com.belairline.repository.AncillaryPurchaseRepository;
import com.belairline.repository.BookingAgentRepository;
import com.belairline.repository.BookingRepository;
import com.belairline.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private BookingAgentRepository bookingAgentRepository;

    @Autowired
    private AncillaryPurchaseRepository ancillaryPurchaseRepository;

    // A2 + D1 - safe saving
    public String saveBooking(Booking booking) {
        // load the real passenger and booking agent from the ids in the JSON
        if (booking.getPassenger() != null && booking.getPassenger().getId() != null) {
            Optional<Passenger> passenger = passengerRepository.findById(booking.getPassenger().getId());
            if (passenger.isEmpty()) {
                return "The passenger with that id does not exist";
            }
            booking.setPassenger(passenger.get());
        }
        if (booking.getAgent() != null && booking.getAgent().getId() != null) {
            Optional<BookingAgent> agent = bookingAgentRepository.findById(booking.getAgent().getId());
            if (agent.isEmpty()) {
                return "The booking agent with that id does not exist";
            }
            booking.setAgent(agent.get());
        }

        // rule 1: no date in the past
        if (booking.getTravelDate() == null || booking.getTravelDate().isBefore(LocalDate.now())) {
            return "Booking date cannot be in the past";
        }
        // rule 2: the agent cannot have another active (not CANCELLED) booking that day
        if (booking.getAgent() != null && bookingRepository.existsByAgentAndTravelDateAndStatusNot(
                booking.getAgent(), booking.getTravelDate(), BookingStatus.CANCELLED)) {
            return "Agent already has a booking on that date";
        }
        // rule 3: a new booking always starts RESERVED
        booking.setStatus(BookingStatus.RESERVED);
        bookingRepository.save(booking);
        return "Booking saved successfully";
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(UUID id) {
        return bookingRepository.findById(id);
    }

    // general update: does NOT change the status (D2)
    public String updateBooking(UUID id, Booking newBooking) {
        Optional<Booking> existing = bookingRepository.findById(id);
        if (existing.isEmpty()) {
            return "The booking with that id does not exist";
        }
        Booking booking = existing.get();
        booking.setTravelDate(newBooking.getTravelDate());
        booking.setFlightNumber(newBooking.getFlightNumber());
        booking.setFareClass(newBooking.getFareClass());
        if (newBooking.getPassenger() != null && newBooking.getPassenger().getId() != null) {
            Optional<Passenger> passenger = passengerRepository.findById(newBooking.getPassenger().getId());
            if (passenger.isEmpty()) {
                return "The passenger with that id does not exist";
            }
            booking.setPassenger(passenger.get());
        }
        if (newBooking.getAgent() != null && newBooking.getAgent().getId() != null) {
            Optional<BookingAgent> agent = bookingAgentRepository.findById(newBooking.getAgent().getId());
            if (agent.isEmpty()) {
                return "The booking agent with that id does not exist";
            }
            booking.setAgent(agent.get());
        }
        bookingRepository.save(booking);
        return "Booking updated successfully";
    }

    // A2 + E3 - a booking with ancillary purchases is protected
    public String deleteBooking(UUID id) {
        Optional<Booking> existing = bookingRepository.findById(id);
        if (existing.isEmpty()) {
            return "The booking with that id does not exist";
        }
        if (ancillaryPurchaseRepository.existsByBooking(existing.get())) {
            return "That booking has ancillary purchases, delete them first";
        }
        bookingRepository.deleteById(id);
        return "Booking deleted successfully";
    }

    // B2
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatusOrderByTravelDateAsc(status);
    }

    // B3
    public List<Booking> getBookingsBetween(LocalDate start, LocalDate end) {
        return bookingRepository.findByTravelDateBetweenOrderByTravelDateAsc(start, end);
    }

    // D2 - status flow
    public String changeStatus(UUID id, BookingStatus newStatus) {
        Optional<Booking> existing = bookingRepository.findById(id);
        if (existing.isEmpty()) {
            return "The booking with that id does not exist";
        }
        Booking booking = existing.get();
        BookingStatus current = booking.getStatus();

        boolean allowed = false;
        if (current == BookingStatus.RESERVED) {
            allowed = newStatus == BookingStatus.PAID || newStatus == BookingStatus.CANCELLED;
        } else if (current == BookingStatus.PAID) {
            allowed = newStatus == BookingStatus.TICKETED || newStatus == BookingStatus.CANCELLED;
        }
        // TICKETED and CANCELLED are final: allowed stays false

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }
        booking.setStatus(newStatus);
        bookingRepository.save(booking);
        return "Booking status updated successfully";
    }

    // F1
    public List<Object[]> getStatsByStatus() {
        return bookingRepository.countBookingsByStatus();
    }

    // F2 - one UPDATE query
    @Transactional
    public String cancelDay(UUID bookingAgentId, LocalDate date) {
        int count = bookingRepository.cancelDay(bookingAgentId, date,
                BookingStatus.CANCELLED, BookingStatus.TICKETED);
        return count + " bookings cancelled";
    }

    // Bonus - page by page
    public Page<Booking> getBookingsPage(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }
}
