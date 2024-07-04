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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        if (isDuplicateFlight(flightRequest) ){
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
                || airport.getAirport().isBlank()) {
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
        String trimmedSearch = search.trim().toLowerCase();
        return databaseAirportRepository.findAirportsBySearchTerm(trimmedSearch);
    }

    @Override
    public PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest) {
        PageResult<Flight> result = new PageResult<>(0,0,new ArrayList<Flight>());

        if (searchFlightsRequest.getFrom() == null || searchFlightsRequest.getTo() == null ||
                searchFlightsRequest.getDepartureDate() == null || searchFlightsRequest.getFrom().equals(searchFlightsRequest.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime startOfDay = searchFlightsRequest.getDepartureDate();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        List<Flight> flights = databaseFlightRepository.findFlightsByFromAirportAndToAirportAndDepartureTime(searchFlightsRequest.getFrom(), searchFlightsRequest.getTo(), startOfDay, endOfDay);

        System.out.println(flights);

        result.setTotalItems(flights.size());
        result.setItems(flights);
        result.setPage(flights.size()/5);

        return result;
    }
}
