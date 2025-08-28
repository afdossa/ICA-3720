package Source;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {

    // Add a session and automatically enroll the creator
    public void addSession(Session session, int studentId) {
        String sql = "INSERT INTO Sessions (course_id, scheduled_time, location) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // insert into Sessions
            stmt.setInt(1, session.getCourseId());
            stmt.setTimestamp(2, Timestamp.valueOf(session.getTime()));
            stmt.setString(3, session.getLocation());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int sessionId = rs.getInt(1);
                    session.setId(sessionId);

                    // also enroll the creator into SessionParticipants
                    String sql2 = "INSERT INTO SessionParticipants (session_id, student_id) VALUES (?, ?)";
                    try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                        stmt2.setInt(1, sessionId);
                        stmt2.setInt(2, studentId);
                        stmt2.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all sessions a student is enrolled in
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

    // Cancel session and remove participants
    public void cancelSession(int sessionId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // remove participants first (if cascade not set in DB)
            String sql1 = "DELETE FROM SessionParticipants WHERE session_id = ?";
            try (PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
                stmt1.setInt(1, sessionId);
                stmt1.executeUpdate();
            }

            // remove the session itself
            String sql2 = "DELETE FROM Sessions WHERE session_id = ?";
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setInt(1, sessionId);
                stmt2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
