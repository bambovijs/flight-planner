package io.codelex.flightplanner.repositories;

import io.codelex.flightplanner.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseAirportRepository extends JpaRepository<Airport, String> {
    List<Airport> findAirportsByAirportContainingIgnoreCaseOrCityContainingIgnoreCaseOrCountryContainingIgnoreCase(String airport,String city,String country);

    Optional<Airport> findByAirportAndCityAndCountry(String airport, String city, String country);

    default Airport findOrCreate(String airport, String city, String country) {
        return findByAirportAndCityAndCountry(airport, city, country)
                .orElseGet(() -> save(new Airport(country, city, airport)));
    }
}
