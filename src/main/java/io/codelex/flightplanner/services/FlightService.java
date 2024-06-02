package io.codelex.flightplanner.services;

import java.util.List;

import io.codelex.flightplanner.controllers.SearchFlightsRequest;
import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.PageResult;
import org.springframework.stereotype.Service;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.repositories.FlightRepository;

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

    public List<Airport> searchAirports(String search) {
        return flightRepository.searchAirports(search);
    }

    public PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest) {
        PageResult<Flight> flights = flightRepository.searchFlights(searchFlightsRequest);
        return flights;
    }
}
