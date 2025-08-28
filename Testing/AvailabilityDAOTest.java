import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

import Source.Availability;
import Source.AvailabilityDAO;

class AvailabilityDAOTest {
    private AvailabilityDAO availabilityDAO;

    @BeforeEach
    void setup() {
        availabilityDAO = new AvailabilityDAO();
    }

    @Test
    void testAddAvailability() {
        Availability a = new Availability(0, 1,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2));
        availabilityDAO.addAvailability(a);
        assertTrue(a.getId() > 0, "Availability should be assigned an ID after insert");
    }

    @Test
    void testGetAvailabilityByInvalidStudent() {
        List<Availability> avails = availabilityDAO.getAvailabilityByStudent(-1);
        assertTrue(avails.isEmpty(), "Invalid student should return empty availability list");
    }
}
