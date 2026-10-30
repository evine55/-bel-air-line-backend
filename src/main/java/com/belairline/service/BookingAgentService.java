package com.belairline.service;

import com.belairline.model.BookingAgent;
import com.belairline.model.Route;
import com.belairline.model.SalesDesk;
import com.belairline.repository.BookingAgentRepository;
import com.belairline.repository.RouteRepository;
import com.belairline.repository.SalesDeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingAgentService {

    @Autowired
    private BookingAgentRepository bookingAgentRepository;

    @Autowired
    private SalesDeskRepository salesDeskRepository;

    @Autowired
    private RouteRepository routeRepository;

    public String saveBookingAgent(BookingAgent agent) {
        // the JSON only carries ids, so load the real sales desk and routes from the database
        if (agent.getSalesDesk() != null && agent.getSalesDesk().getId() != null) {
            Optional<SalesDesk> desk = salesDeskRepository.findById(agent.getSalesDesk().getId());
            if (desk.isEmpty()) {
                return "The sales desk with that id does not exist";
            }
            agent.setSalesDesk(desk.get());
        }
        List<Route> realRoutes = new ArrayList<>();
        if (agent.getRoutes() != null) {
            for (Route r : agent.getRoutes()) {
                Optional<Route> route = routeRepository.findById(r.getId());
                if (route.isEmpty()) {
                    return "That route does not exist";
                }
                realRoutes.add(route.get());
            }
        }
        agent.setRoutes(realRoutes);
        bookingAgentRepository.save(agent);
        return "Booking agent saved successfully";
    }

    public List<BookingAgent> getAllBookingAgents() {
        return bookingAgentRepository.findAll();
    }

    public Optional<BookingAgent> getBookingAgentById(UUID id) {
        return bookingAgentRepository.findById(id);
    }

    // changes the personal fields; sales desk and routes have their own endpoints (D3, D4)
    public String updateBookingAgent(UUID id, BookingAgent newAgent) {
        Optional<BookingAgent> existing = bookingAgentRepository.findById(id);
        if (existing.isEmpty()) {
            return "The booking agent with that id does not exist";
        }
        BookingAgent agent = existing.get();
        agent.setFirstName(newAgent.getFirstName());
        agent.setLastName(newAgent.getLastName());
        agent.setEmail(newAgent.getEmail());
        bookingAgentRepository.save(agent);
        return "Booking agent updated successfully";
    }

    public String deleteBookingAgent(UUID id) {
        if (!bookingAgentRepository.existsById(id)) {
            return "The booking agent with that id does not exist";
        }
        bookingAgentRepository.deleteById(id);
        return "Booking agent deleted successfully";
    }

    // C1
    public List<BookingAgent> getBookingAgentsByRoute(String name) {
        return bookingAgentRepository.findByRouteName(name);
    }

    // D3 - add one route, keep the old ones, save the owner (BookingAgent)
    public String addRouteToBookingAgent(UUID bookingAgentId, UUID routeId) {
        Optional<BookingAgent> agentOpt = bookingAgentRepository.findById(bookingAgentId);
        if (agentOpt.isEmpty()) {
            return "The booking agent with that id does not exist";
        }
        Optional<Route> routeOpt = routeRepository.findById(routeId);
        if (routeOpt.isEmpty()) {
            return "That route does not exist";
        }
        BookingAgent agent = agentOpt.get();
        Route route = routeOpt.get();
        if (agent.getRoutes().contains(route)) {
            return "The booking agent already has that route";
        }
        agent.getRoutes().add(route);
        bookingAgentRepository.save(agent);
        return "Route added to the booking agent";
    }

    // D4 - move a booking agent to another sales desk
    public String changeSalesDesk(UUID bookingAgentId, int deskNumber) {
        Optional<BookingAgent> agentOpt = bookingAgentRepository.findById(bookingAgentId);
        if (agentOpt.isEmpty()) {
            return "The booking agent with that id does not exist";
        }
        Optional<SalesDesk> deskOpt = salesDeskRepository.findByDeskNumber(deskNumber);
        if (deskOpt.isEmpty()) {
            return "The sales desk with that deskNumber does not exist";
        }
        BookingAgent agent = agentOpt.get();
        SalesDesk desk = deskOpt.get();
        boolean alreadyMine = agent.getSalesDesk() != null && agent.getSalesDesk().getId().equals(desk.getId());
        if (!alreadyMine && bookingAgentRepository.existsBySalesDesk(desk)) {
            return "That sales desk is taken by another booking agent";
        }
        agent.setSalesDesk(desk);
        bookingAgentRepository.save(agent);
        return "Sales desk changed successfully";
    }
}
