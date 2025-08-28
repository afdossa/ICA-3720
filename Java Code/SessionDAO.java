package edu.clemson.studybuddy.dao;

import edu.clemson.studybuddy.DatabaseConnection;
import edu.clemson.studybuddy.model.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {
    public void addSession(Session session) {
        String sql = "INSERT INTO Sessions (course_id, scheduled_time, location) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, session.getCourseId());
            stmt.setTimestamp(2, Timestamp.valueOf(session.getTime()));
            stmt.setString(3, session.getLocation());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    session.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Session> getSessionsByStudent(int studentId) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT s.session_id, s.course_id, s.scheduled_time, s.location " +
                "FROM Sessions s " +
                "JOIN SessionParticipants sp ON s.session_id = sp.session_id " +
                "WHERE sp.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sessions.add(new Session(
                        rs.getInt("session_id"),
                        rs.getInt("course_id"),
                        rs.getTimestamp("scheduled_time").toLocalDateTime(),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public void cancelSession(int sessionId) {
        String sql = "DELETE FROM Sessions WHERE session_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sessionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
