package edu.clemson.studybuddy.model;

public class Match {
    private int id;
    private int studentA;
    private int studentB;
    private int courseId;

    public Match(int id, int studentA, int studentB, int courseId) {
        this.id = id;
        this.studentA = studentA;
        this.studentB = studentB;
        this.courseId = courseId;
    }

    public int getId() { return id; }
    public int getStudentA() { return studentA; }
    public int getStudentB() { return studentB; }
    public int getCourseId() { return courseId; }
}
