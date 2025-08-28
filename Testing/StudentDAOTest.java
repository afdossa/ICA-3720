import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Source.StudentDAO;
import Source.Student;
import Source.DatabaseConnection;

public class StudentDAOTest {
    private StudentDAO studentDAO;

    @BeforeEach
    void setup() {
        studentDAO = new StudentDAO();
        // Clean up any leftover test data
        DatabaseConnection.executeUpdate("DELETE FROM Students WHERE email LIKE 'junit%'");
    }

    @AfterEach
    void cleanup() {
        DatabaseConnection.executeUpdate("DELETE FROM Students WHERE email LIKE 'junit%'");
    }

    @Test
    void testAddStudentAssignsId() {
        Student s = new Student(0, "JUnitUser", "junit1@test.com");
        studentDAO.addStudent(s);
        assertTrue(s.getId() > 0, "Student ID should be assigned after insert");
    }
}
