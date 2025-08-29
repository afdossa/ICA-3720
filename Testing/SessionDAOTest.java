import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

import Source.Session;
import Source.SessionDAO;
import Source.DatabaseConnection;

class SessionDAOTests {
    private SessionDAO sessionDAO;

    @BeforeEach
    void setup() {
        sessionDAO = new SessionDAO();
    }

    @AfterEach
    void cleanup() {
        DatabaseConnection.executeUpdate("DELETE FROM Sessions WHERE location LIKE 'JUnit%' OR location LIKE 'Lab%' OR location LIKE 'Room%' OR location LIKE 'Cancel%'");
        DatabaseConnection.executeUpdate("DELETE FROM SessionParticipants WHERE session_id NOT IN (SELECT session_id FROM Sessions)");
    }

    @Test
    void testAddSessionAssignsId() {
        Session s = new Session(0, 1,
                LocalDateTime.now().plusDays(1),
                "JUnit Room");
        sessionDAO.addSession(s, 1);
        assertTrue(s.getId() > 0, "Session should be assigned an ID after insert");
    }

    @Test
    void testGetSessionsByInvalidStudent() {
        List<Session> sessions = sessionDAO.getSessionsByStudent(-1);
        assertTrue(sessions.isEmpty(), "Invalid student should return empty session list");
    }

    @Test
    void testAddSessionEnrollsCreator() {
        Session s = new Session(0, 2,
                LocalDateTime.now().plusDays(2),
                "Lab 101");
        sessionDAO.addSession(s, 5); // student 5 is creator

        List<Session> sessions = sessionDAO.getSessionsByStudent(5);
        assertFalse(sessions.isEmpty(), "Creator should be automatically enrolled in session");
        assertEquals("Lab 101", sessions.get(0).getLocation());
    }

    @Test
    void testCancelSessionRemovesIt() {
        Session s = new Session(0, 3,
                LocalDateTime.now().plusDays(3),
                "Cancel Room");
        sessionDAO.addSession(s, 7);

        int sessionId = s.getId();
        sessionDAO.cancelSession(sessionId);

        List<Session> sessions = sessionDAO.getSessionsByStudent(7);
        assertTrue(sessions.isEmpty(), "Cancelled session should not appear for student");
    }

    @Test
    void testMultipleSessionsForStudent() {
        Session s1 = new Session(0, 4,
                LocalDateTime.now().plusDays(4),
                "Room A");
        Session s2 = new Session(0, 4,
                LocalDateTime.now().plusDays(5),
                "Room B");

        sessionDAO.addSession(s1, 10);
        sessionDAO.addSession(s2, 10);

        List<Session> sessions = sessionDAO.getSessionsByStudent(10);
        assertTrue(sessions.size() >= 2, "Student should have multiple sessions");
    }
}
