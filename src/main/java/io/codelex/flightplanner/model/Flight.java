package io.codelex.flightplanner.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_airport", nullable = false)
    private Airport from;
    @ManyToOne
    @JoinColumn(name = "to_airport", nullable = false)
    private Airport to;
    private String carrier;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;

    private static final AtomicLong idGenerator = new AtomicLong(0);

    public Flight(Airport from, Airport to, String carrier, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.id = idGenerator.incrementAndGet();
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Flight() {

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
        if (carrier == null || carrier.isBlank()) {
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
                airport.getCountry().isBlank()|| airport.getCity().isBlank() || airport.getAirport().isBlank();
    }
}
