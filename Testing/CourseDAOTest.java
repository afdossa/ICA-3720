import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import Source.Course;      // <-- your Course model
import Source.CourseDAO;  // <-- your DAO

class CourseDAOTest {
    private CourseDAO courseDAO;

    @BeforeEach
    void setup() {
        courseDAO = new CourseDAO();
    }

    @Test
    void testAddCourse() {
        Course c = new Course(0, "JUNIT101", "JUnit Course");
        courseDAO.addCourse(c);
        assertTrue(c.getId() > 0);
    }

    @Test
    void testGetCoursesByStudentEmpty() {
        List<Course> courses = courseDAO.getCoursesByStudent(-1); // invalid student
        assertTrue(courses.isEmpty());
    }
}
