package io.codelex.flightplanner.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.repositories.FlightRepository;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void addFlight(Flight flight) {
        flightRepository.addFlight(flight);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.getAllFlights();
    }

    public boolean deleteFlight(long id) {
        return flightRepository.deleteFlight(id);
    }

    public Flight getFlightById(long id) {
        return flightRepository.getFlightById(id);
    }

    public void clearFlights() {
        flightRepository.clearFlights();
    }
}
