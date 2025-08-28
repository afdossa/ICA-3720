import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SessionDAOTest {
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private SessionDAO sessionDAO;
    private int studentId;
    private int courseId;

    @BeforeEach
    void setup() {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        sessionDAO = new SessionDAO();

        Student s = new Student(0, "Session User", "junitSession@test.com");
        studentDAO.addStudent(s);
        studentId = s.getId();

        Course c = new Course(0, "JUNIT201", "SessionCourse");
        courseDAO.addCourse(c);
        courseId = c.getId();
        courseDAO.enrollStudentInCourse(studentId, courseId);
    }

    @AfterEach
    void cleanup() {
        DatabaseConnection.executeUpdate("DELETE FROM Sessions WHERE location LIKE 'JUnit%'");
        DatabaseConnection.executeUpdate("DELETE FROM Courses WHERE course_code LIKE 'JUNIT%'");
        DatabaseConnection.executeUpdate("DELETE FROM Students WHERE email LIKE 'junit%'");
    }

    @Test void testAddSession() {
        Session s = new Session(0, courseId, LocalDateTime.now(), "JUnit Loc");
        sessionDAO.addSession(s, studentId);
        assertTrue(s.getId() > 0);
    }

    @Test void testGetSessionsByStudent() {
        Session s = new Session(0, courseId, LocalDateTime.now(), "JUnit2");
        sessionDAO.addSession(s, studentId);
        assertFalse(sessionDAO.getSessionsByStudent(studentId).isEmpty());
    }

    @Test void testCancelSession() {
        Session s = new Session(0, courseId, LocalDateTime.now(), "JUnitCancel");
        sessionDAO.addSession(s, studentId);
        sessionDAO.cancelSession(s.getId());
        assertTrue(sessionDAO.getSessionsByStudent(studentId).isEmpty());
    }

    @Test void testMultipleSessions() {
        sessionDAO.addSession(new Session(0, courseId, LocalDateTime.now(), "JUnitA"), studentId);
        sessionDAO.addSession(new Session(0, courseId, LocalDateTime.now(), "JUnitB"), studentId);
        assertTrue(sessionDAO.getSessionsByStudent(studentId).size() >= 2);
    }

    @Test void testInvalidCourseSessionFailsGracefully() {
        Session s = new Session(0, -1, LocalDateTime.now(), "BadCourse");
        assertDoesNotThrow(() -> sessionDAO.addSession(s, studentId));
    }

    @Test void testSessionLocationNotNull() {
        Session s = new Session(0, courseId, LocalDateTime.now(), "JUnitLoc");
        sessionDAO.addSession(s, studentId);
        assertNotNull(sessionDAO.getSessionsByStudent(studentId).get(0).getLocation());
    }

    @Test void testCancelNonexistentSession() {
        assertDoesNotThrow(() -> sessionDAO.cancelSession(-1));
    }

    @Test void testRetrieveEmptySessionsForNewStudent() {
        Student s = new Student(0, "Empty", "empty@test.com");
        studentDAO.addStudent(s);
        assertTrue(sessionDAO.getSessionsByStudent(s.getId()).isEmpty());
    }

    @Test void testSessionTimeSavedCorrectly() {
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);
        Session s = new Session(0, courseId, now, "JUnitTime");
        sessionDAO.addSession(s, studentId);
        assertEquals(now, sessionDAO.getSessionsByStudent(studentId).get(0).getTime());
    }

    @Test void testSessionPersistenceAcrossFetches() {
        Session s = new Session(0, courseId, LocalDateTime.now(), "JUnitPersist");
        sessionDAO.addSession(s, studentId);
        int sid = s.getId();
        assertTrue(sessionDAO.getSessionsByStudent(studentId)
                .stream().anyMatch(se -> se.getId() == sid));
    }
}
