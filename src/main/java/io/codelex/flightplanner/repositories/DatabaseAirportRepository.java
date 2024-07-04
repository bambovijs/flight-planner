package io.codelex.flightplanner.repositories;

import io.codelex.flightplanner.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseAirportRepository extends JpaRepository<Airport, String> {
    List<Airport> findAirportsByAirportContainingIgnoreCaseOrCityContainingIgnoreCaseOrCountryContainingIgnoreCase(String airport,String city,String country);
}
