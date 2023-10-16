package com.example.klassroom;

import java.sql.Date;
import java.time.LocalDate;

public class StudentAttendance {
    private String studentUsername;
    private java.sql.Date date;
    private String classroomCode;
    private boolean isPresent;

    public StudentAttendance(String studentUsername, LocalDate date, String classroomCode, boolean isPresent) {
        this.studentUsername = studentUsername;
        this.date = Date.valueOf(date);
        this.classroomCode = classroomCode;
        this.isPresent = isPresent;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean selected) {
        this.isPresent=selected;
    }

    public LocalDate getDate() {
        return date.toLocalDate();
    }


    // Getter and setter methods for the class variables
    // Add getters and setters for all class variables
}
