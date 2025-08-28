package edu.clemson.studybuddy.model;

import java.time.LocalDateTime;

public class Session {
    private int id;
    private int courseId;
    private LocalDateTime time;
    private String location;

    public Session(int id, int courseId, LocalDateTime time, String location) {
        this.id = id;
        this.courseId = courseId;
        this.time = time;
        this.location = location;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }   // ✅ add this

    public int getCourseId() { return courseId; }
    public LocalDateTime getTime() { return time; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", time=" + time +
                ", location='" + location + '\'' +
                '}';
    }
}
