package com.belairline.service;

import com.belairline.model.SalesDesk;
import com.belairline.repository.SalesDeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalesDeskService {

    @Autowired
    private SalesDeskRepository salesDeskRepository;

    public String saveSalesDesk(SalesDesk salesDesk) {
        salesDeskRepository.save(salesDesk);
        return "Sales desk saved successfully";
    }

    public List<SalesDesk> getAllSalesDesks() {
        return salesDeskRepository.findAll();
    }

    public Optional<SalesDesk> getSalesDeskById(UUID id) {
        return salesDeskRepository.findById(id);
    }

    public String updateSalesDesk(UUID id, SalesDesk newDesk) {
        Optional<SalesDesk> existing = salesDeskRepository.findById(id);
        if (existing.isEmpty()) {
            return "The sales desk with that id does not exist";
        }
        SalesDesk desk = existing.get();
        desk.setDeskNumber(newDesk.getDeskNumber());
        desk.setName(newDesk.getName());
        salesDeskRepository.save(desk);
        return "Sales desk updated successfully";
    }

    public String deleteSalesDesk(UUID id) {
        if (!salesDeskRepository.existsById(id)) {
            return "The sales desk with that id does not exist";
        }
        salesDeskRepository.deleteById(id);
        return "Sales desk deleted successfully";
    }

    // C2
    public List<SalesDesk> getFreeSalesDesks() {
        return salesDeskRepository.findFreeSalesDesks();
    }
}
