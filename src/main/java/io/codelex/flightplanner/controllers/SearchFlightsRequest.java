package io.codelex.flightplanner.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SearchFlightsRequest {
    @NotNull
    private String from;
    @NotNull
    private String to;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate departureDate;

    public SearchFlightsRequest(String from, String to,@JsonFormat(pattern="yyyy-MM-dd") LocalDate departureDate) {
        this.from = from;
        this.to = to;
        this.departureDate = departureDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate.atStartOfDay();
    }
}