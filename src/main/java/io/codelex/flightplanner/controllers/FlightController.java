package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.services.FlightService;
import java.util.List;

@RestController
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/admin-api/flights/{id}")
    public Flight getFlight(@PathVariable long id) {
        return flightService.getFlightById(id);
    }

    @GetMapping("/api/flights/{id}")
    public SearchFlightsResponse findFlight(@PathVariable long id) {
        return new SearchFlightsResponse(flightService.getFlightById(id));
    }

    @PutMapping("/admin-api/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse addFlight(@RequestBody FlightRequest flightRequest) {
        return flightService.addFlight(flightRequest);
    }

    @DeleteMapping("/admin-api/flights/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlight(@PathVariable long id) {
        flightService.deleteFlight(id);
    }

    @PostMapping("/api/flights/search")
    @ResponseStatus(HttpStatus.OK)
    public PageResult<Flight> searchFlights(@RequestBody SearchFlightsRequest searchFlightsRequest) {
        return flightService.searchFlights(searchFlightsRequest);
    }

    @GetMapping("/api/airports")
    @ResponseStatus(HttpStatus.OK)
    public List<Airport> searchAirports(@RequestParam() String search) {
        return flightService.searchAirports(search);
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
