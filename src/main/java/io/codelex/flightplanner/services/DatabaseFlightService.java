package io.codelex.flightplanner.services;

import io.codelex.flightplanner.controllers.AddFlightRequest;
import io.codelex.flightplanner.controllers.SearchFlightsRequest;
import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;
import io.codelex.flightplanner.repositories.DatabaseAirportRepository;
import io.codelex.flightplanner.repositories.DatabaseFlightRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class DatabaseFlightService implements FlightService{

    private final DatabaseFlightRepository databaseFlightRepository;
    private final DatabaseAirportRepository databaseAirportRepository;

    public DatabaseFlightService(DatabaseFlightRepository databaseFlightRepository, DatabaseAirportRepository databaseAirportRepository) {
        this.databaseFlightRepository = databaseFlightRepository;
        this.databaseAirportRepository = databaseAirportRepository;
    }

    @Override
    public Flight getFlightById(long id) {
        Flight flight = databaseFlightRepository.findById(id).orElse(null);

        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return flight;
    }

    @Override
    public void clearFlights() {
        databaseFlightRepository.deleteAll();
    }

    @Override
    public synchronized Flight addFlight(AddFlightRequest flightRequest) {
        if (flightRequest.getFrom() == null || flightRequest.getTo() == null ||
                flightRequest.getCarrier() == null || flightRequest.getCarrier().trim().isEmpty() ||
                flightRequest.getDepartureTime() == null || flightRequest.getArrivalTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (isDuplicateFlight(flightRequest)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        validateAirport(flightRequest.getFrom());
        validateAirport(flightRequest.getTo());

        if (areSameAirport(flightRequest.getFrom(), flightRequest.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (flightRequest.getDepartureTime().isAfter(flightRequest.getArrivalTime()) || flightRequest.getDepartureTime().isEqual(flightRequest.getArrivalTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Airport from_airport = new Airport(flightRequest.getFrom().getCountry(), flightRequest.getFrom().getCity(), flightRequest.getFrom().getAirport());
        databaseAirportRepository.save(from_airport);

        Airport to_airport = new Airport(flightRequest.getTo().getCountry(), flightRequest.getTo().getCity(), flightRequest.getTo().getAirport());
        databaseAirportRepository.save(to_airport);

        Flight flight = new Flight(from_airport, to_airport, flightRequest.getCarrier(),
                flightRequest.getDepartureTime(), flightRequest.getArrivalTime());
        databaseFlightRepository.save(flight);

        return flight;
    }

    private void validateAirport(Airport airport){
        if (airport.getCountry() == null || airport.getCity() == null || airport.getAirport() == null ||
                airport.getCountry().isBlank() || airport.getCity().isBlank()
                || airport.getAirport().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isDuplicateFlight(AddFlightRequest flightRequest) {
        return databaseFlightRepository.isDuplicateFlight(flightRequest.getFrom().getAirport(), flightRequest.getTo().getAirport(), flightRequest.getCarrier(), flightRequest.getDepartureTime(), flightRequest.getArrivalTime());
    }

    private boolean areSameAirport(Airport from, Airport to) {
        return from.getAirport().trim().equalsIgnoreCase(to.getAirport().trim());
    }

    @Override
    public synchronized void deleteFlight(long id) {
        databaseFlightRepository.deleteById(id);
    }

    @Override
    public List<Airport> searchAirports(String search) {
        System.out.println("search: " + search);
        System.out.println("databaseAirportRepository: " + databaseAirportRepository.searchAirports(search));
        return databaseAirportRepository.searchAirports(search);
    }

    @Override
    public PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest) {
        return null;
    }
}
