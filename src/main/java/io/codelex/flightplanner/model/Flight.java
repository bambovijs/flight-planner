package io.codelex.flightplanner.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Flight {
    private Long id;
    private Airport departure;
    private Airport destination;
    private String carrier;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;

    private static final AtomicLong idGenerator = new AtomicLong(0);

    public Flight(Airport departure, Airport destination, String carrier, String departureTime, String arrivalTime) {
        this.id = idGenerator.incrementAndGet();
        this.departure = departure;
        this.destination = destination;
        this.carrier = carrier;
        this.departureTime = convertStringToLocalDateTime(departureTime);
        this.arrivalTime = convertStringToLocalDateTime(arrivalTime);
    }

    public static LocalDateTime convertStringToLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Airport getDeparture() {
        return departure;
    }

    public void setDeparture(Airport departure) {
        if (departure == null || isInvalidAirport(departure)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        this.departure = departure;
    }

    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
        if (destination == null || isInvalidAirport(destination)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        this.destination = destination;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        if (carrier == null || carrier.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        this.carrier = carrier;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        if (departureTime == null) {
            throw new IllegalArgumentException("Departure time cannot be null or empty");
        }

        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        if (arrivalTime == null) {
            throw new IllegalArgumentException("Arrival time cannot be null or empty");
        }
        this.arrivalTime = arrivalTime;
    }

    private boolean isInvalidAirport(Airport airport) {
        return airport.getCountry() == null || airport.getCity() == null || airport.getAirport() == null ||
                airport.getCountry().trim().isEmpty() || airport.getCity().trim().isEmpty() || airport.getAirport().trim().isEmpty();
    }
}
