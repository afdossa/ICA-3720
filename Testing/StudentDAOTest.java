import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import Source.Student;
import Source.StudentDAO;

class StudentDAOTest {
    private StudentDAO studentDAO;

    @BeforeEach
    void setup() {
        studentDAO = new StudentDAO();
    }

    @Test
    void testAddStudentAssignsId() {
        Student s = new Student(0, "JUnit Student", "junit@student.com");
        studentDAO.addStudent(s);
        assertTrue(s.getId() > 0);
    }

    @Test
    void testGetAllStudentsNotEmptyAfterInsert() {
        Student s = new Student(0, "Another JUnit", "another@student.com");
        studentDAO.addStudent(s);
        List<Student> students = studentDAO.getAllStudents();
        assertFalse(students.isEmpty());
    }
}
