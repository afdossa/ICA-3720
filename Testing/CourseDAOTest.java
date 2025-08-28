import Source.CourseDAO;
import Source.Student;
import Source.StudentDAO;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CourseDAOTest {
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private int studentId;

    @BeforeEach
    void setup() {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        Student s = new Student(0, "Course User", "junitCourse@test.com");
        studentDAO.addStudent(s);
        studentId = s.getId();
    }

    @AfterEach
    void cleanup() {
        DatabaseConnection.executeUpdate("DELETE FROM Courses WHERE course_code LIKE 'JUNIT%'");
        DatabaseConnection.executeUpdate("DELETE FROM Students WHERE email LIKE 'junit%'");
    }

    @Test void testAddCourseAssignsId() {
        Course c = new Course(0, "JUNIT101", "JUnit Basics");
        courseDAO.addCourse(c);
        assertTrue(c.getId() > 0);
    }

    @Test void testEnrollStudentInCourse() {
        Course c = new Course(0, "JUNIT102", "Advanced JUnit");
        courseDAO.addCourse(c);
        courseDAO.enrollStudentInCourse(studentId, c.getId());
        assertTrue(courseDAO.getCoursesByStudent(studentId).stream()
                .anyMatch(co -> co.getCode().equals("JUNIT102")));
    }

    @Test void testGetCoursesByStudentEmptyInitially() {
        assertTrue(courseDAO.getCoursesByStudent(studentId).isEmpty());
    }

    @Test void testGetStudentsByCourse() {
        Course c = new Course(0, "JUNIT103", "Testing");
        courseDAO.addCourse(c);
        courseDAO.enrollStudentInCourse(studentId, c.getId());
        assertTrue(courseDAO.getStudentsByCourse("JUNIT103").stream()
                .anyMatch(st -> st.getId() == studentId));
    }

    @Test void testMultipleStudentsSameCourse() {
        Course c = new Course(0, "JUNIT104", "Multi");
        courseDAO.addCourse(c);

        Student s2 = new Student(0, "Another", "junitAnother@test.com");
        studentDAO.addStudent(s2);

        courseDAO.enrollStudentInCourse(studentId, c.getId());
        courseDAO.enrollStudentInCourse(s2.getId(), c.getId());

        assertEquals(2, courseDAO.getStudentsByCourse("JUNIT104").size());
    }

    @Test void testDuplicateEnroll() {
        Course c = new Course(0, "JUNIT105", "Dup");
        courseDAO.addCourse(c);
        courseDAO.enrollStudentInCourse(studentId, c.getId());
        courseDAO.enrollStudentInCourse(studentId, c.getId()); // should not crash
        assertEquals(1, courseDAO.getCoursesByStudent(studentId).size());
    }

    @Test void testInvalidStudentEnrollFailsGracefully() {
        Course c = new Course(0, "JUNIT106", "BadEnroll");
        courseDAO.addCourse(c);
        assertDoesNotThrow(() -> courseDAO.enrollStudentInCourse(-1, c.getId()));
    }

    @Test void testCourseWithEmptyCode() {
        Course c = new Course(0, "", "NoCode");
        courseDAO.addCourse(c);
        assertTrue(c.getId() > 0);
    }

    @Test void testGetStudentsByNonexistentCourse() {
        assertTrue(courseDAO.getStudentsByCourse("XXX").isEmpty());
    }

    @Test void testGetCoursesByInvalidStudent() {
        assertTrue(courseDAO.getCoursesByStudent(-1).isEmpty());
    }
}
