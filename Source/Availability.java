package Source;

import java.time.LocalDateTime;

public class Availability {
    private int id;
    private int studentId;
    private LocalDateTime start;
    private LocalDateTime end;

    public Availability(int id, int studentId, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.studentId = studentId;
        this.start = start;
        this.end = end;
    }

    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }

    @Override
    public String toString() {
        return "Availability{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
