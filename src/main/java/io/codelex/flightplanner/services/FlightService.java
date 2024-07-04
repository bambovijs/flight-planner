package io.codelex.flightplanner.services;

import io.codelex.flightplanner.controllers.AddFlightRequest;
import io.codelex.flightplanner.controllers.SearchFlightsRequest;
import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;

import java.util.List;

public interface FlightService {
    Flight getFlightById(long id);
    void clearFlights();

    Flight addFlight(AddFlightRequest flightRequest);

    void deleteFlight(long id);
    List<Airport> searchAirport(String search);
    PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest);
}
