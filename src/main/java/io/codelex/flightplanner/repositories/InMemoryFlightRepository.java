package io.codelex.flightplanner.repositories;

import io.codelex.flightplanner.controllers.SearchFlightsRequest;
import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryFlightRepository implements FlightRepository {

    private final List<Flight> flights = new ArrayList<>();

    @Override
    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    @Override
    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    @Override
    public Flight getFlightById(long id) {
        return flights.stream()
                .filter(flight -> flight.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void clearFlights() {
        if (!flights.isEmpty()) {
            flights.clear();
        }
    }

    @Override
    public boolean deleteFlight(long id) {
        flights.removeIf(flight -> flight.getId() == id);
        return true;
    }

    @Override
    public List<Airport> searchAirports(String search) {
        String lowerCaseSearch = search.trim().toLowerCase();
        List<Airport> airports = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getDeparture().getAirport().toLowerCase().contains(lowerCaseSearch) ||
                    flight.getDeparture().getCity().toLowerCase().contains(lowerCaseSearch) ||
                    flight.getDeparture().getCountry().toLowerCase().contains(lowerCaseSearch)) {
                airports.add(flight.getDeparture());
            }
            if (flight.getDestination().getAirport().toLowerCase().contains(lowerCaseSearch) ||
                    flight.getDestination().getCity().toLowerCase().contains(lowerCaseSearch) ||
                    flight.getDestination().getCountry().toLowerCase().contains(lowerCaseSearch)) {
                airports.add(flight.getDestination());
            }
        }
        return airports.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Airport> getAllAirports() {
        return flights.stream()
                .flatMap(flight -> Stream.of(flight.getDeparture(), flight.getDestination()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest) {
        List<Flight> matchingFlights = flights.stream()
                .filter(flight -> flight.getDeparture().getAirport().equalsIgnoreCase(searchFlightsRequest.getFrom().getAirport())
                        && flight.getDestination().getAirport().equalsIgnoreCase(searchFlightsRequest.getTo().getAirport())
                        && flight.getDepartureTime().toString().startsWith(searchFlightsRequest.getDepartureDate()))
                .collect(Collectors.toList());

        return new PageResult<Flight>(0, matchingFlights.size(), matchingFlights);
    }

}