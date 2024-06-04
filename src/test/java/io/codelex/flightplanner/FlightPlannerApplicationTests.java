package io.codelex.flightplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.codelex.flightplanner.repositories.FlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.codelex.flightplanner.repositories.InMemoryFlightRepository;

@SpringBootTest
class FlightPlannerApplicationTests {

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void testClearFlights() {
        flightRepository.clearFlights();

        assertEquals(0, flightRepository.getAllFlights().size());
    }
}
