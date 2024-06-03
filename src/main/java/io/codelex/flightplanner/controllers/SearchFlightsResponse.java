package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;

import java.time.format.DateTimeFormatter;

public class SearchFlightsResponse {
    private Long id;
    private Airport from;
    private Airport to;
    private String carrier;
    private String departureTime;
    private String arrivalTime;

    public SearchFlightsResponse(Flight flight) {
        this.arrivalTime = flight.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.carrier = flight.getCarrier();
        this.departureTime = flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.from = flight.getFrom();
        this.id = flight.getId();
        this.to = flight.getTo();
    }

    public Long getId() {
        return id;
    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFrom(Airport from) {
        this.from = from;
    }

    public void setTo(Airport to) {
        this.to = to;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
