package io.codelex.flightplanner.repositories;

import io.codelex.flightplanner.controllers.SearchFlightsRequest;
import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryFlightRepository {

    private final List<Flight> flights = new ArrayList<>();

    public void addFlight(Flight flight) {
        flights.add(flight);
    }


    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    public Flight getFlightById(long id) {
        return flights.stream()
                .filter(flight -> flight.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void clearFlights() {
        if (!flights.isEmpty()) {
            flights.clear();
        }
    }

    public void deleteFlight(long id) {
        flights.removeIf(flight -> flight.getId() == id);
    }

    public List<Airport> searchAirports(String search) {
        String lowerCaseSearch = search.trim().toLowerCase();
        List<Airport> airports = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getFrom().getAirport().toLowerCase().contains(lowerCaseSearch) ||
                    flight.getFrom().getCity().toLowerCase().contains(lowerCaseSearch) ||
                    flight.getFrom().getCountry().toLowerCase().contains(lowerCaseSearch)) {
                airports.add(flight.getFrom());
            }
            if (flight.getTo().getAirport().toLowerCase().contains(lowerCaseSearch) ||
                    flight.getTo().getCity().toLowerCase().contains(lowerCaseSearch) ||
                    flight.getTo().getCountry().toLowerCase().contains(lowerCaseSearch)) {
                airports.add(flight.getTo());
            }
        }
        return airports.stream().distinct().collect(Collectors.toList());
    }

    public List<Airport> getAllAirports() {
        return flights.stream()
                .flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo()))
                .distinct()
                .collect(Collectors.toList());
    }

    public PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest) {
        List<Flight> matchingFlights = flights.stream()
                .filter(flight -> flight.getFrom().getAirport().equalsIgnoreCase(searchFlightsRequest.getFrom())
                        && flight.getTo().getAirport().equalsIgnoreCase(searchFlightsRequest.getTo())
                        && flight.getDepartureTime().toString().startsWith(searchFlightsRequest.getDepartureDate().toString()))
                .collect(Collectors.toList());

        return new PageResult<Flight>(0, matchingFlights.size(), matchingFlights);
    }

}