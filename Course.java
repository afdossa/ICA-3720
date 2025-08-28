package edu.clemson.studybuddy.model;

public class Course {
    private int id;
    private String code;
    private String name;

    public Course(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return id + ": " + code + " - " + name;
    }
}
