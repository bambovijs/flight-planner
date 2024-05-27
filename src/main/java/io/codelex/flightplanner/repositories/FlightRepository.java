package io.codelex.flightplanner.repositories;

import io.codelex.flightplanner.model.Flight;

import java.util.List;

public interface FlightRepository {
    void addFlight(Flight flight);
    List<Flight> getAllFlights();
    Flight getFlightById(long id);
    void clearFlights();
    boolean deleteFlight(long id);
}

