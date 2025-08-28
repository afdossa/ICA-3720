package edu.clemson.studybuddy.service;

import edu.clemson.studybuddy.DatabaseConnection;
import edu.clemson.studybuddy.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchService {
    public List<Student> suggestMatches(int studentId) {
        List<Student> matches = new ArrayList<>();

        String sql = """
            SELECT DISTINCT s.student_id, s.name, s.email
            FROM Students s
            JOIN StudentCourses sc ON s.student_id = sc.student_id
            JOIN Availability a ON s.student_id = a.student_id
            WHERE sc.course_id IN (
                SELECT course_id FROM StudentCourses WHERE student_id = ?
            )
            AND s.student_id != ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                matches.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matches;
    }
}
