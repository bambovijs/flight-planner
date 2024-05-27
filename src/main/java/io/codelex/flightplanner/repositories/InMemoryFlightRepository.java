package io.codelex.flightplanner.repositories;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryFlightRepository implements FlightRepository {

    private final List<Flight> flights = new ArrayList<>();

    {
         Airport airport1 = new Airport("Latvia", "Riga", "RIX");
         Airport airport2 = new Airport("Germany", "Berlin", "TXL");
         flights.add(new Flight(airport1, airport2, "C", "2019-01-01 00:00", "2019-01-01 00:00"));
    }

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
        if(flights.isEmpty()){
            System.out.println("Flight list is already empty");
        }else{
            flights.clear();
        }
    }

    @Override
    public boolean deleteFlight(long id) {
        return flights.removeIf(flight -> flight.getId() == id);
    }
}