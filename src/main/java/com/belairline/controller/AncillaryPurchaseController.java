package com.belairline.controller;

import com.belairline.model.AncillaryPurchase;
import com.belairline.service.AncillaryPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ancillary-purchases")
public class AncillaryPurchaseController {

    @Autowired
    private AncillaryPurchaseService ancillaryPurchaseService;

    // E1
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID bookingId, @RequestBody AncillaryPurchase purchase) {
        String message = ancillaryPurchaseService.saveAncillaryPurchase(bookingId, purchase);
        if (message.equals("The booking with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("Ancillary purchases are only for TICKETED bookings")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AncillaryPurchase>> getAll() {
        return new ResponseEntity<>(ancillaryPurchaseService.getAllAncillaryPurchases(), HttpStatus.OK);
    }

    // E2
    @GetMapping("/passenger/{passportNumber}")
    public ResponseEntity<List<AncillaryPurchase>> getHistory(@PathVariable String passportNumber) {
        return new ResponseEntity<>(ancillaryPurchaseService.getHistoryOfPassenger(passportNumber), HttpStatus.OK);
    }

    // E3
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = ancillaryPurchaseService.deleteAncillaryPurchase(id);
        if (message.equals("The ancillary purchase with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
