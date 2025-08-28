import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import Source.StudentDAO;
import Source.AvailabilityDAO;
import Source.Availability;
import Source.DatabaseConnection;
import Source.Student;

public class AvailabilityDAOTest {
    private StudentDAO studentDAO;
    private AvailabilityDAO availabilityDAO;
    private int studentId;

    @BeforeEach
    void setup() {
        studentDAO = new StudentDAO();
        availabilityDAO = new AvailabilityDAO();

        // create a fresh student for each test
        Student s = new Student(0, "AvailUser", "junitAvail@test.com");
        studentDAO.addStudent(s);
        studentId = s.getId();
    }

    @AfterEach
    void cleanup() {
        DatabaseConnection.executeUpdate("DELETE FROM Availability WHERE student_id=" + studentId);
        DatabaseConnection.executeUpdate("DELETE FROM Students WHERE email LIKE 'junit%'");
    }

    @Test
    void testAddAvailability() {
        Availability a = new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0));
        availabilityDAO.addAvailability(a);
        assertTrue(a.getId() > 0);
    }

    @Test
    void testGetAvailabilityByStudent() {
        availabilityDAO.addAvailability(new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 2, 9, 0),
                LocalDateTime.of(2025, 1, 2, 11, 0)));
        List<Availability> avails = availabilityDAO.getAvailabilityByStudent(studentId);
        assertFalse(avails.isEmpty());
    }

    @Test
    void testMultipleAvailabilityEntries() {
        availabilityDAO.addAvailability(new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 3, 10, 0),
                LocalDateTime.of(2025, 1, 3, 12, 0)));
        availabilityDAO.addAvailability(new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 4, 10, 0),
                LocalDateTime.of(2025, 1, 4, 12, 0)));
        assertTrue(availabilityDAO.getAvailabilityByStudent(studentId).size() >= 2);
    }

    @Test
    void testInvalidTimeRange() {
        Availability bad = new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 5, 15, 0),
                LocalDateTime.of(2025, 1, 5, 10, 0));

        // ⚠️ this will only pass if AvailabilityDAO checks for invalid ranges
        assertThrows(IllegalArgumentException.class,
                () -> availabilityDAO.addAvailability(bad));
    }

    @Test
    void testDeleteAvailability() {
        Availability a = new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 6, 10, 0),
                LocalDateTime.of(2025, 1, 6, 12, 0));
        availabilityDAO.addAvailability(a);
        availabilityDAO.deleteAvailability(a.getId());
        assertTrue(availabilityDAO.getAvailabilityByStudent(studentId).isEmpty());
    }

    @Test
    void testAvailabilityOverlapsAllowed() {
        availabilityDAO.addAvailability(new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 7, 10, 0),
                LocalDateTime.of(2025, 1, 7, 12, 0)));
        availabilityDAO.addAvailability(new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 7, 11, 0),
                LocalDateTime.of(2025, 1, 7, 13, 0)));
        assertEquals(2, availabilityDAO.getAvailabilityByStudent(studentId).size());
    }

    @Test
    void testEmptyAvailabilityListForNewStudent() {
        assertTrue(availabilityDAO.getAvailabilityByStudent(-1).isEmpty());
    }

    @Test
    void testStartEqualsEndTime() {
        Availability a = new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 8, 10, 0),
                LocalDateTime.of(2025, 1, 8, 10, 0));
        availabilityDAO.addAvailability(a);
        assertTrue(a.getId() > 0);
    }

    @Test
    void testAvailabilityPersistenceAcrossFetches() {
        Availability a = new Availability(0, studentId,
                LocalDateTime.of(2025, 1, 9, 10, 0),
                LocalDateTime.of(2025, 1, 9, 12, 0));
        availabilityDAO.addAvailability(a);
        int id = a.getId();
        assertTrue(availabilityDAO.getAvailabilityByStudent(studentId)
                .stream().anyMatch(av -> av.getId() == id));
    }

    @Test
    void testDeleteNonexistentAvailability() {
        assertDoesNotThrow(() -> availabilityDAO.deleteAvailability(-1));
    }
}
