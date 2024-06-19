package io.codelex.flightplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.codelex.flightplanner.repositories.InMemoryFlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FlightPlannerApplicationTests {

    @Autowired
    private InMemoryFlightRepository flightRepository;

    @Test
    public void testClearFlights() {
        flightRepository.clearFlights();

        assertEquals(0, flightRepository.getAllFlights().size());
    }
}
