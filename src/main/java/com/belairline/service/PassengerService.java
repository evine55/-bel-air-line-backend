package com.belairline.service;

import com.belairline.model.Passenger;
import com.belairline.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public String savePassenger(Passenger passenger) {
        passengerRepository.save(passenger);
        return "Passenger saved successfully";
    }

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Optional<Passenger> getPassengerById(UUID id) {
        return passengerRepository.findById(id);
    }

    public String updatePassenger(UUID id, Passenger newPassenger) {
        Optional<Passenger> existing = passengerRepository.findById(id);
        if (existing.isEmpty()) {
            return "The passenger with that id does not exist";
        }
        Passenger passenger = existing.get();
        passenger.setFirstName(newPassenger.getFirstName());
        passenger.setLastName(newPassenger.getLastName());
        passenger.setPassportNumber(newPassenger.getPassportNumber());
        passenger.setEmail(newPassenger.getEmail());
        passenger.setPhone(newPassenger.getPhone());
        passengerRepository.save(passenger);
        return "Passenger updated successfully";
    }

    public String deletePassenger(UUID id) {
        if (!passengerRepository.existsById(id)) {
            return "The passenger with that id does not exist";
        }
        passengerRepository.deleteById(id);
        return "Passenger deleted successfully";
    }

    // B1
    public List<Passenger> getPassengersByLastName(String lastName) {
        return passengerRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    // C4 - the controller checks the booking agent exists first
    public List<Passenger> getPassengersOfBookingAgent(UUID bookingAgentId) {
        return passengerRepository.findPassengersOfBookingAgent(bookingAgentId);
    }
}
