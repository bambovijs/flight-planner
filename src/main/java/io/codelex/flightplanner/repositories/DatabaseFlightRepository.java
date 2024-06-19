package io.codelex.flightplanner.repositories;

import io.codelex.flightplanner.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DatabaseFlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Flight f WHERE f.from.airport = :fromAirport AND f.to.airport = :toAirport AND f.carrier = :carrier AND f.departureTime = :departureTime AND f.arrivalTime = :arrivalTime")
    boolean isDuplicateFlight(@Param("fromAirport") String fromAirport, @Param("toAirport") String toAirport, @Param("carrier") String carrier, @Param("departureTime") LocalDateTime departureTime, @Param("arrivalTime") LocalDateTime arrivalTime);

    @Query("SELECT f FROM Flight f WHERE f.from.airport = :fromAirport AND f.to.airport = :toAirport AND f.departureTime >= :departureDate")
    List<Flight> searchFlights(@Param("fromAirport") String fromAirport, @Param("toAirport") String toAirport, @Param("departureDate") LocalDateTime departureDate);
}
