package com.belairline.service;

import com.belairline.model.AncillaryPurchase;
import com.belairline.model.Booking;
import com.belairline.model.BookingStatus;
import com.belairline.repository.AncillaryPurchaseRepository;
import com.belairline.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AncillaryPurchaseService {

    @Autowired
    private AncillaryPurchaseRepository ancillaryPurchaseRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // E1
    public String saveAncillaryPurchase(UUID bookingId, AncillaryPurchase purchase) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return "The booking with that id does not exist";
        }
        Booking booking = bookingOpt.get();
        if (booking.getStatus() != BookingStatus.TICKETED) {
            return "Ancillary purchases are only for TICKETED bookings";
        }
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setBooking(booking);
        ancillaryPurchaseRepository.save(purchase);
        return "Ancillary purchase saved successfully";
    }

    public List<AncillaryPurchase> getAllAncillaryPurchases() {
        return ancillaryPurchaseRepository.findAll();
    }

    // E2
    public List<AncillaryPurchase> getHistoryOfPassenger(String passportNumber) {
        return ancillaryPurchaseRepository.findHistoryOfPassenger(passportNumber);
    }

    // E3
    public String deleteAncillaryPurchase(UUID id) {
        if (!ancillaryPurchaseRepository.existsById(id)) {
            return "The ancillary purchase with that id does not exist";
        }
        ancillaryPurchaseRepository.deleteById(id);
        return "Ancillary purchase deleted successfully";
    }
}
