CREATE DATABASE studybuddy;
USE studybuddy;

-- Students
CREATE TABLE Students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Courses
CREATE TABLE Courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20) NOT NULL,
    course_name VARCHAR(100) NOT NULL
);

-- StudentCourses
CREATE TABLE StudentCourses (
    student_id INT,
    course_id INT,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES Students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id) ON DELETE CASCADE
);

-- Availability
CREATE TABLE Availability (
    availability_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    FOREIGN KEY (student_id) REFERENCES Students(student_id) ON DELETE CASCADE
);

-- Sessions
CREATE TABLE Sessions (
    session_id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT,
    scheduled_time DATETIME NOT NULL,
    location VARCHAR(100),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id) ON DELETE CASCADE
);

-- SessionParticipants
CREATE TABLE SessionParticipants (
    session_id INT,
    student_id INT,
    PRIMARY KEY (session_id, student_id),
    FOREIGN KEY (session_id) REFERENCES Sessions(session_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES Students(student_id) ON DELETE CASCADE
);

-- Matches
CREATE TABLE Matches (
    match_id INT PRIMARY KEY AUTO_INCREMENT,
    student_a_id INT,
    student_b_id INT,
    course_id INT,
    FOREIGN KEY (student_a_id) REFERENCES Students(student_id),
    FOREIGN KEY (student_b_id) REFERENCES Students(student_id),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);
