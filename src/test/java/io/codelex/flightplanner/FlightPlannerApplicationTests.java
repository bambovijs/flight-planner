package io.codelex.flightplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.codelex.flightplanner.repositories.InMemoryFlightRepository;

@SpringBootTest
class FlightPlannerApplicationTests {

    @Test
    public void testClearFlights() {
        InMemoryFlightRepository flightRepository = new InMemoryFlightRepository();
        
        // Call the clearFlights method
        flightRepository.clearFlights();
        
        // Check if the flights are cleared successfully
        assertEquals(0, flightRepository.getAllFlights().size());
    }

}
