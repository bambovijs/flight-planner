package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.repositories.DatabaseAirportRepository;
import io.codelex.flightplanner.repositories.DatabaseFlightRepository;
import io.codelex.flightplanner.repositories.InMemoryFlightRepository;
import io.codelex.flightplanner.services.DatabaseFlightService;
import io.codelex.flightplanner.services.FlightService;
import io.codelex.flightplanner.services.InMemoryFlightService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightModeConfiguration {

    @Value("${flight.storage.mode}")
    private String flightStorageMode;

    @Bean
    public FlightService createFlightRepositoryBean(InMemoryFlightRepository inMemoryFlightRepository, DatabaseFlightRepository databaseFlightRepository, DatabaseAirportRepository databaseAirportRepository) {
        if (flightStorageMode.equals("in-memory")) {
            return new InMemoryFlightService(inMemoryFlightRepository);
        } else {
            return new DatabaseFlightService(databaseFlightRepository, databaseAirportRepository);
        }
    }
}
