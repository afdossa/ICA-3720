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
    public void setId(int id) { this.id = id; }   // needed for DAO after insert

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return id + ": " + code + " - " + name;
    }
}
