import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

import Source.Session;
import Source.SessionDAO;

class SessionDAOTest {
    private SessionDAO sessionDAO;

    @BeforeEach
    void setup() {
        sessionDAO = new SessionDAO();
    }

    @Test
    void testAddSession() {
        Session session = new Session(0, 1, LocalDateTime.now(), "JUnit Room");
        sessionDAO.addSession(session, 1);  // assume student with ID 1 exists
        assertTrue(session.getId() > 0);
    }

    @Test
    void testGetSessionsByInvalidStudent() {
        List<Session> sessions = sessionDAO.getSessionsByStudent(-1);
        assertTrue(sessions.isEmpty());
    }
}
