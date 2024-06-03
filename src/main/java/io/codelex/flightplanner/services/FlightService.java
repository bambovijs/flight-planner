package io.codelex.flightplanner.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.codelex.flightplanner.controllers.FlightRequest;
import io.codelex.flightplanner.controllers.FlightResponse;
import io.codelex.flightplanner.controllers.SearchFlightsRequest;
import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.PageResult;
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

    public synchronized FlightResponse addFlight(FlightRequest flightRequest){
        validateFlightRequest(flightRequest);

        if (isDuplicateFlight(flightRequest)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Flight flight = new Flight(flightRequest.getFrom(), flightRequest.getTo(), flightRequest.getCarrier(),
                flightRequest.getDepartureTime(), flightRequest.getArrivalTime());
        flightRepository.addFlight(flight);

        return new FlightResponse(flight);
    }
    private void validateFlightRequest(FlightRequest flightRequest) {
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

        LocalDateTime departureTime = Flight.convertStringToLocalDateTime(flightRequest.getDepartureTime());
        LocalDateTime arrivalTime = Flight.convertStringToLocalDateTime(flightRequest.getArrivalTime());
        if (departureTime.isAfter(arrivalTime) || departureTime.isEqual(arrivalTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isDuplicateFlight(FlightRequest flightRequest) {
        return flightRepository.getAllFlights().stream()
                .anyMatch(flight -> flight.getDeparture().getAirport().equals(flightRequest.getFrom().getAirport()) &&
                        flight.getDestination().getAirport().equals(flightRequest.getTo().getAirport()) &&
                        flight.getCarrier().equals(flightRequest.getCarrier()) &&
                        convertLocalDateTimeToString(flight.getDepartureTime()).equals(flightRequest.getDepartureTime())
                        &&
                        convertLocalDateTimeToString(flight.getArrivalTime()).equals(flightRequest.getArrivalTime()));
    }

    private String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private void validateAirport(Airport airport) {
        if (airport.getCountry() == null || airport.getCity() == null || airport.getAirport() == null ||
                airport.getCountry().trim().isEmpty() || airport.getCity().trim().isEmpty()
                || airport.getAirport().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean areSameAirport(Airport from, Airport to) {
        return from.getAirport().trim().equalsIgnoreCase(to.getAirport().trim());
    }

    public List<Flight> getAllFlights() {
        return flightRepository.getAllFlights();
    }

    public synchronized boolean deleteFlight(long id) {
        return flightRepository.deleteFlight(id);
    }

    public Flight getFlightById(long id) {
        Flight flight = flightRepository.getFlightById(id);

        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return flight;
    }

    public void clearFlights() {
        flightRepository.clearFlights();
    }

    public List<Airport> searchAirports(String search) {
        return flightRepository.searchAirports(search);
    }

    public PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest) {
        validateSearchRequest(searchFlightsRequest);
        PageResult<Flight> flights = flightRepository.searchFlights(searchFlightsRequest);
        return flights;
    }

    protected void validateSearchRequest(SearchFlightsRequest request) {
        if (request.getFrom() == null || request.getTo() == null || request.getDepartureDate() == null ||
                request.getFrom().toString().trim().isEmpty() || request.getTo().toString().trim().isEmpty() || request.getDepartureDate().trim().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid search parameters");
        }
        else if(request.getTo().getAirport().equals(request.getFrom().getAirport())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid search parameters");
        }
    }
}
