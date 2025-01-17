package io.codelex.flightplanner.services;

import java.util.List;
import io.codelex.flightplanner.controllers.AddFlightRequest;
import io.codelex.flightplanner.controllers.SearchFlightsRequest;
import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.PageResult;
import org.springframework.http.HttpStatus;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.repositories.InMemoryFlightRepository;
import org.springframework.web.server.ResponseStatusException;

public class InMemoryFlightService implements FlightService {
    private final InMemoryFlightRepository flightRepository;

    public InMemoryFlightService(InMemoryFlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public synchronized Flight addFlight(AddFlightRequest flightRequest){
        validateFlightRequest(flightRequest);

        if (isDuplicateFlight(flightRequest)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Flight flight = new Flight(flightRequest.getFrom(), flightRequest.getTo(), flightRequest.getCarrier(),
                flightRequest.getDepartureTime(), flightRequest.getArrivalTime());
        flightRepository.addFlight(flight);

        return flight;
    }

    private void validateFlightRequest(AddFlightRequest flightRequest) {
        if (flightRequest.getFrom() == null || flightRequest.getTo() == null ||
                flightRequest.getCarrier() == null || flightRequest.getCarrier().trim().isEmpty() ||
                flightRequest.getDepartureTime() == null || flightRequest.getArrivalTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        validateAirport(flightRequest.getFrom());
        validateAirport(flightRequest.getTo());

        if (areSameAirport(flightRequest.getFrom(), flightRequest.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (flightRequest.getDepartureTime().isAfter(flightRequest.getArrivalTime()) || flightRequest.getDepartureTime().isEqual(flightRequest.getArrivalTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isDuplicateFlight(AddFlightRequest flightRequest) {
        return flightRepository.getAllFlights().stream()
                .anyMatch(flight -> flight.getFrom().getAirport().equals(flightRequest.getFrom().getAirport()) &&
                        flight.getTo().getAirport().equals(flightRequest.getTo().getAirport()) &&
                        flight.getCarrier().equals(flightRequest.getCarrier()) &&
                        flight.getDepartureTime().equals(flightRequest.getDepartureTime()) &&
                        flight.getArrivalTime().equals(flightRequest.getArrivalTime()));
    }

    private void validateAirport(Airport airport) {
        if (airport.getCountry() == null || airport.getCity() == null || airport.getAirport() == null ||
                airport.getCountry().isBlank() || airport.getCity().isBlank()
                || airport.getAirport().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean areSameAirport(Airport from, Airport to) {
        return from.getAirport().trim().equalsIgnoreCase(to.getAirport().trim());
    }
    @Override
    public synchronized void deleteFlight(long id) {
        flightRepository.deleteFlight(id);
    }
    @Override
    public Flight getFlightById(long id) {
        Flight flight = flightRepository.getFlightById(id);

        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return flight;
    }
    @Override
    public void clearFlights() {
        flightRepository.clearFlights();
    }
    @Override
    public List<Airport> searchAirport(String search) {
        return flightRepository.searchAirports(search);
    }
    @Override
    public PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest) {
        validateSearchRequest(searchFlightsRequest);
        return flightRepository.searchFlights(searchFlightsRequest);
    }

    protected void validateSearchRequest(SearchFlightsRequest request) {
        if (request.getFrom() == null || request.getTo() == null || request.getDepartureDate() == null ||
                request.getFrom().isBlank()|| request.getTo().isBlank() || request.getTo().equals(request.getFrom())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid search parameters");
        }
    }
}
