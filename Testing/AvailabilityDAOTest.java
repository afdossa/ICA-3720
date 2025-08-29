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
        // No exception = pass
        assertNotNull(a, "Availability object should not be null after insert");
    }

    @Test
    void testGetAvailabilityByInvalidStudent() {
        List<Availability> avails = availabilityDAO.getAvailabilityByStudent(-1);
        assertTrue(avails.isEmpty(), "Invalid student should return empty availability list");
    }

    @Test
    void testGetAvailabilityByStudentAfterInsert() {
        Availability a = new Availability(0, 2,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2));
        availabilityDAO.addAvailability(a);

        List<Availability> avails = availabilityDAO.getAvailabilityByStudent(2);
        assertFalse(avails.isEmpty(), "Student 2 should have at least one availability");
    }

    @Test
    void testMultipleAvailabilitiesForSameStudent() {
        Availability a1 = new Availability(0, 3,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1));
        Availability a2 = new Availability(0, 3,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1));
        availabilityDAO.addAvailability(a1);
        availabilityDAO.addAvailability(a2);

        List<Availability> avails = availabilityDAO.getAvailabilityByStudent(3);
        assertTrue(avails.size() >= 2, "Student 3 should have multiple availabilities");
    }

    @Test
    void testStartBeforeEnd() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Availability a = new Availability(0, 4, start, end);

        availabilityDAO.addAvailability(a);
        List<Availability> avails = availabilityDAO.getAvailabilityByStudent(4);

        assertFalse(avails.isEmpty(), "Valid availability should be stored");
        assertTrue(avails.get(0).getStart().isBefore(avails.get(0).getEnd()),
                "Start time should be before end time");
    }
}
