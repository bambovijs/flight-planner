package io.codelex.flightplanner.repositories;

import io.codelex.flightplanner.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseAirportRepository extends JpaRepository<Airport, String> {
    @Query("SELECT a FROM Airport a WHERE LOWER(a.airport) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.city) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.country) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Airport> findAirportsBySearchTerm(@Param("search") String search);
}
