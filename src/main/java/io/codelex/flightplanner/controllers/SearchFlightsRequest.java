package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.model.Airport;

public class SearchFlightsRequest {
    private Airport from;
    private Airport to;
    private String departureDate;

    public SearchFlightsRequest(Airport from, Airport to, String departureDate) {
        this.from = from;
        this.to = to;
        this.departureDate = departureDate;
    }

    public Airport getFrom() {
        return from;
    }

    public void setFrom(Airport from) {
        this.from = from;
    }

    public Airport getTo() {
        return to;
    }

    public void setTo(Airport to) {
        this.to = to;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
}