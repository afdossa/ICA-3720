import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest {
    private StudentDAO studentDAO;

    @BeforeEach
    void setup() { studentDAO = new StudentDAO(); }

    @AfterEach
    void cleanup() {
        DatabaseConnection.executeUpdate("DELETE FROM Students WHERE email LIKE 'junit%'");
    }

    @Test void testAddStudentAssignsId() {
        Student s = new Student(0, "JUnit1", "junit1@test.com");
        studentDAO.addStudent(s);
        assertTrue(s.getId() > 0);
    }

    @Test void testRetrieveStudentInList() {
        Student s = new Student(0, "JUnit2", "junit2@test.com");
        studentDAO.addStudent(s);
        assertTrue(studentDAO.getAllStudents().stream()
                .anyMatch(st -> st.getEmail().equals("junit2@test.com")));
    }

    @Test void testMultipleStudentsAdded() {
        studentDAO.addStudent(new Student(0, "S1", "junit3@test.com"));
        studentDAO.addStudent(new Student(0, "S2", "junit4@test.com"));
        assertTrue(studentDAO.getAllStudents().size() >= 2);
    }

    @Test void testGetStudentById() {
        Student s = new Student(0, "JUnit3", "junit5@test.com");
        studentDAO.addStudent(s);
        Student found = studentDAO.getStudentById(s.getId());
        assertEquals("JUnit3", found.getName());
    }

    @Test void testGetInvalidStudentById() {
        assertNull(studentDAO.getStudentById(-1));
    }

    @Test void testAddStudentWithEmptyName() {
        Student s = new Student(0, "", "junit6@test.com");
        studentDAO.addStudent(s);
        assertTrue(s.getId() > 0);
    }

    @Test void testAddStudentWithInvalidEmail() {
        Student s = new Student(0, "JUnitBadEmail", "not-an-email");
        studentDAO.addStudent(s);
        assertTrue(s.getId() > 0);
    }

    @Test void testUniqueEmailsAllowed() {
        studentDAO.addStudent(new Student(0, "A", "junit7@test.com"));
        studentDAO.addStudent(new Student(0, "B", "junit8@test.com"));
        assertTrue(studentDAO.getAllStudents().size() >= 2);
    }

    @Test void testDeleteStudent() {
        Student s = new Student(0, "JUnitDelete", "junit9@test.com");
        studentDAO.addStudent(s);
        DatabaseConnection.executeUpdate("DELETE FROM Students WHERE student_id=" + s.getId());
        assertNull(studentDAO.getStudentById(s.getId()));
    }

    @Test void testGetAllStudentsNotNull() {
        assertNotNull(studentDAO.getAllStudents());
    }
}
