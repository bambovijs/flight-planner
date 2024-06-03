package io.codelex.flightplanner.repositories;

import io.codelex.flightplanner.controllers.SearchFlightsRequest;
import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;

import java.util.List;

public interface FlightRepository {
    void addFlight(Flight flight);

    List<Flight> getAllFlights();

    Flight getFlightById(long id);

    void clearFlights();

    void deleteFlight(long id);

    List<Airport> searchAirports(String search);

    List<Airport> getAllAirports();

    PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest);
}

