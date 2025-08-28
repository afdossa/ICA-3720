import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private SessionDAO sessionDAO;
    private AvailabilityDAO availabilityDAO;

    private int student1;
    private int student2;
    private int courseId;

    @BeforeEach
    void setup() {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        sessionDAO = new SessionDAO();
        availabilityDAO = new AvailabilityDAO();

        Student s1 = new Student(0, "Stu1", "junitStu1@test.com");
        Student s2 = new Student(0, "Stu2", "junitStu2@test.com");
        studentDAO.addStudent(s1);
        studentDAO.addStudent(s2);
        student1 = s1.getId();
        student2 = s2.getId();

        Course c = new Course(0, "JUNIT301", "IntegrationCourse");
        courseDAO.addCourse(c);
        courseId = c.getId();
        courseDAO.enrollStudentInCourse(student1, courseId);
        courseDAO.enrollStudentInCourse(student2, courseId);
    }

    @AfterEach
    void cleanup() {
        DatabaseConnection.executeUpdate("DELETE FROM Sessions WHERE location LIKE 'JUnit%'");
        DatabaseConnection.executeUpdate("DELETE FROM Courses WHERE course_code LIKE 'JUNIT%'");
        DatabaseConnection.executeUpdate("DELETE FROM Students WHERE email LIKE 'junit%'");
        DatabaseConnection.executeUpdate("DELETE FROM Availability WHERE student_id IN (" + student1 + "," + student2 + ")");
    }

    @Test void testFullWorkflowSingleStudent() {
        Availability a = new Availability(0, student1,
                LocalDateTime.of(2025,1,1,10,0),
                LocalDateTime.of(2025,1,1,12,0));
        availabilityDAO.addAvailability(a);

        Session s = new Session(0, courseId,
                LocalDateTime.of(2025,1,1,11,0), "JUnitLocation");
        sessionDAO.addSession(s, student1);

        List<Session> sessions = sessionDAO.getSessionsByStudent(student1);
        assertEquals(1, sessions.size());
    }

    @Test void testTwoStudentsSameCourse() {
        courseDAO.enrollStudentInCourse(student2, courseId);
        assertEquals(2, courseDAO.getStudentsByCourse("JUNIT301").size());
    }

    @Test void testAvailabilityOverlapMatch() {
        availabilityDAO.addAvailability(new Availability(0, student1,
                LocalDateTime.of(2025,1,2,10,0),
                LocalDateTime.of(2025,1,2,12,0)));
        availabilityDAO.addAvailability(new Availability(0, student2,
                LocalDateTime.of(2025,1,2,11,0),
                LocalDateTime.of(2025,1,2,13,0)));

        assertEquals(1, availabilityDAO.getAvailabilityByStudent(student1).size());
        assertEquals(1, availabilityDAO.getAvailabilityByStudent(student2).size());
    }

    @Test void testSessionLinksToCourse() {
        Session s = new Session(0, courseId, LocalDateTime.now(), "JUnitIntegration");
        sessionDAO.addSession(s, student1);
        assertEquals(courseId, s.getCourseId());
    }

    @Test void testCancelSessionIntegration() {
        Session s = new Session(0, courseId, LocalDateTime.now(), "JUnitCancelInt");
        sessionDAO.addSession(s, student1);
        sessionDAO.cancelSession(s.getId());
        assertTrue(sessionDAO.getSessionsByStudent(student1).isEmpty());
    }

    @Test void testMultipleSessionsAcrossStudents() {
        sessionDAO.addSession(new Session(0, courseId, LocalDateTime.now(), "JUnit1"), student1);
        sessionDAO.addSession(new Session(0, courseId, LocalDateTime.now(), "JUnit2"), student2);
        assertEquals(2, sessionDAO.getSessionsByStudent(student1).size() +
                sessionDAO.getSessionsByStudent(student2).size());
    }

    @Test void testStressAdd50Students() {
        for (int i = 0; i < 50; i++) {
            Student s = new Student(0, "Stress" + i, "junitStress" + i + "@test.com");
            studentDAO.addStudent(s);
            courseDAO.enrollStudentInCourse(s.getId(), courseId);
        }
        assertTrue(courseDAO.getStudentsByCourse("JUNIT301").size() >= 50);
    }

    @Test void testEndToEndFlow() {
        Student newS = new Student(0, "FlowUser", "flow@test.com");
        studentDAO.addStudent(newS);
        courseDAO.enrollStudentInCourse(newS.getId(), courseId);
        availabilityDAO.addAvailability(new Availability(0, newS.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        Session s = new Session(0, courseId, LocalDateTime.now().plusMinutes(30), "FlowLocation");
        sessionDAO.addSession(s, newS.getId());
        assertFalse(sessionDAO.getSessionsByStudent(newS.getId()).isEmpty());
    }

    @Test void testDeleteCourseCascadesSessions() {
        Session s = new Session(0, courseId, LocalDateTime.now(), "CascadeLoc");
        sessionDAO.addSession(s, student1);
        DatabaseConnection.executeUpdate("DELETE FROM Courses WHERE course_id=" + courseId);
        assertTrue(sessionDAO.getSessionsByStudent(student1).isEmpty());
    }

    @Test void testAvailabilityDeletedWithStudent() {
        availabilityDAO.addAvailability(new Availability(0, student1,
                LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        DatabaseConnection.executeUpdate("DELETE FROM Students WHERE student_id=" + student1);
        assertTrue(availabilityDAO.getAvailabilityByStudent(student1).isEmpty());
    }
}
