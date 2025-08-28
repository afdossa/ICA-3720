import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import Source.SessionDAO;
import Source.Session;
import Source.Student;
import Source.Course;
import Source.DatabaseConnection;

public class SessionDAOTest {
    private SessionDAO sessionDAO;

    @BeforeEach
    void setup() {
        sessionDAO = new SessionDAO();
        // cleanup any leftover sessions
        DatabaseConnection.executeUpdate("DELETE FROM Sessions WHERE location LIKE 'JUnit%'");
    }

    @AfterEach
    void cleanup() {
        DatabaseConnection.executeUpdate("DELETE FROM Sessions WHERE location LIKE 'JUnit%'");
    }

    @Test
    void testAddSessionAssignsId() {
        Session s = new Session(0, 1, LocalDateTime.now().plusDays(1), "JUnitLocation");
        sessionDAO.addSession(s, 1); // studentId = 1 must exist
        assertTrue(s.getId() > 0, "Session ID should be assigned after insert");
    }

    @Test
    void testGetSessionsByInvalidStudent() {
        List<Session> sessions = sessionDAO.getSessionsByStudent(-1);
        assertNotNull(sessions, "Should return empty list, not null");
        assertTrue(sessions.isEmpty());
    }
}
