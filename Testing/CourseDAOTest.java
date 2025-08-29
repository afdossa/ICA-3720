import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import Source.Course;
import Source.CourseDAO;
import Source.Student;

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
        assertTrue(c.getId() > 0, "Course should be assigned an ID after insert");
    }

    @Test
    void testGetCoursesByStudentEmpty() {
        List<Course> courses = courseDAO.getCoursesByStudent(-1); // invalid student
        assertTrue(courses.isEmpty(), "Invalid student should return no courses");
    }

    @Test
    void testEnrollStudentInCourse() {
        Course c = new Course(0, "JUNIT102", "Enroll Test");
        courseDAO.addCourse(c);

        // fake student id 1 for testing
        courseDAO.enrollStudentInCourse(1, c.getId());
        List<Course> courses = courseDAO.getCoursesByStudent(1);

        assertFalse(courses.isEmpty(), "Student should have at least one enrolled course");
        assertEquals("JUNIT102", courses.get(0).getCode());
    }

    @Test
    void testGetStudentsByCourseEmpty() {
        List<Student> students = courseDAO.getStudentsByCourse("FAKECODE");
        assertTrue(students.isEmpty(), "Nonexistent course should return no students");
    }

    @Test
    void testGetAllCourses() {
        Course c1 = new Course(0, "JUNIT201", "Course A");
        Course c2 = new Course(0, "JUNIT202", "Course B");
        courseDAO.addCourse(c1);
        courseDAO.addCourse(c2);

        List<Course> allCourses = courseDAO.getAllCourses();
        assertTrue(allCourses.size() >= 2, "Should have at least 2 courses in the list");
    }
}
