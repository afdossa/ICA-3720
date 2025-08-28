package edu.clemson.studybuddy.dao;

import edu.clemson.studybuddy.DatabaseConnection;
import edu.clemson.studybuddy.model.Availability;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityDAO {
    public void addAvailability(Availability availability) {
        String sql = "INSERT INTO Availability (student_id, start_time, end_time) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, availability.getStudentId());
            stmt.setTimestamp(2, Timestamp.valueOf(availability.getStart()));
            stmt.setTimestamp(3, Timestamp.valueOf(availability.getEnd()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Availability> getAvailabilityByStudent(int studentId) {
        List<Availability> list = new ArrayList<>();
        String sql = "SELECT * FROM Availability WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Availability(
                        rs.getInt("availability_id"),
                        rs.getInt("student_id"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
