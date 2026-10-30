package com.belairline.controller;

import com.belairline.model.SalesDesk;
import com.belairline.service.SalesDeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales-desks")
public class SalesDeskController {

    @Autowired
    private SalesDeskService salesDeskService;

    @PostMapping("/save")
    public ResponseEntity<String> saveSalesDesk(@RequestBody SalesDesk salesDesk) {
        String message = salesDeskService.saveSalesDesk(salesDesk);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SalesDesk>> getAllSalesDesks() {
        return new ResponseEntity<>(salesDeskService.getAllSalesDesks(), HttpStatus.OK);
    }

    // C2
    @GetMapping("/free")
    public ResponseEntity<List<SalesDesk>> getFreeSalesDesks() {
        return new ResponseEntity<>(salesDeskService.getFreeSalesDesks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSalesDeskById(@PathVariable UUID id) {
        Optional<SalesDesk> desk = salesDeskService.getSalesDeskById(id);
        if (desk.isPresent()) {
            return new ResponseEntity<>(desk.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("The sales desk with that id does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSalesDesk(@PathVariable UUID id, @RequestBody SalesDesk salesDesk) {
        String message = salesDeskService.updateSalesDesk(id, salesDesk);
        if (message.equals("The sales desk with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSalesDesk(@PathVariable UUID id) {
        String message = salesDeskService.deleteSalesDesk(id);
        if (message.equals("The sales desk with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
