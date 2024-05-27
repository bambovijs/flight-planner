package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.model.Airport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.services.FlightService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/api/flights/{id}")
    public Flight getFlight(@PathVariable long id) {
//        return flightService.getFlightById(id);
        Flight flight = flightService.getFlightById(id);

        if(flight == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return flight;
    }

    @PutMapping("/admin-api/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse addFlight(@RequestBody FlightRequest flightRequest) {
        validateFlightRequest(flightRequest);

        if (isDuplicateFlight(flightRequest)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Flight flight = new Flight(flightRequest.getFrom(), flightRequest.getTo(), flightRequest.getCarrier(),
                flightRequest.getDepartureTime(), flightRequest.getArrivalTime());
        flightService.addFlight(flight);

        return new FlightResponse(flight);
    }

    @DeleteMapping("/admin-api/flights/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlight(@PathVariable long id) {

        boolean deleted = flightService.deleteFlight(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
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

    private void validateAirport(Airport airport) {
        if (airport.getCountry() == null || airport.getCity() == null || airport.getAirport() == null ||
                airport.getCountry().trim().isEmpty() || airport.getCity().trim().isEmpty() || airport.getAirport().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean areSameAirport(Airport from, Airport to) {
        return from.getAirport().trim().equalsIgnoreCase(to.getAirport().trim());
    }

    private boolean isDuplicateFlight(FlightRequest flightRequest) {
        return flightService.getAllFlights().stream()
                .anyMatch(flight ->
                         flight.getDeparture().getAirport().equals(flightRequest.getFrom().getAirport()) &&
                         flight.getDestination().getAirport().equals(flightRequest.getTo().getAirport()) &&
                         flight.getCarrier().equals(flightRequest.getCarrier()) &&
                         convertLocalDateTimeToString(flight.getDepartureTime()).equals(flightRequest.getDepartureTime())
                         &&
                         convertLocalDateTimeToString(flight.getArrivalTime()).equals(flightRequest.getArrivalTime()));
    }

    private String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @PostMapping("/testing-api/clear")
    public void clearFlights() {
        flightService.clearFlights();
    }
}
