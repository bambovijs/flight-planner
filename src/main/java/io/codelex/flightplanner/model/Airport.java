package io.codelex.flightplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class Airport {
    @Id
    private String airport;
    private String country;
    private String city;

    public Airport(String country, String city, String airport) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }

    public Airport(String airport) {
        this.airport = airport;
    }

    public Airport() {

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }
}