package io.codelex.flightplanner.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Flight {
    private Long id;
    private Airport from;
    private Airport to;
    private String carrier;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;

    private static final AtomicLong idGenerator = new AtomicLong(0);

    public Flight(Airport from, Airport to, String carrier, String departureTime, String arrivalTime) {
        this.id = idGenerator.incrementAndGet();
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = convertStringToLocalDateTime(departureTime);
        this.arrivalTime = convertStringToLocalDateTime(arrivalTime);
    }

    public static LocalDateTime convertStringToLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Airport getFrom() {
        return from;
    }

    public void setFrom(Airport from) {
        if (from == null || isInvalidAirport(from)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        this.from = from;
    }

    public Airport getTo() {
        return to;
    }

    public void setTo(Airport destination) {
        if (destination == null || isInvalidAirport(destination)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        this.to = destination;
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
