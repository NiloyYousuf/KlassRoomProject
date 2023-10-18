package com.example.klassroom;

public class CurrentClassroom {
    // Private fields
    private static int classroomId;
    public static String classroomCode;
    private static String subjectName;
    private static String teacherUsername;

    // Getter and Setter methods for classroomId
    public static int getClassroomId() {
        return classroomId;
    }

    public static void setClassroomId(int id) {
        classroomId = id;
    }

    // Getter and Setter methods for classroomCode


    public static void setClassroomCode(String code) {
        classroomCode = code;
    }

    // Getter and Setter methods for subjectName
    public static String getSubjectName() {
        return subjectName;
    }

    public static void setSubjectName(String subject) {
        subjectName = subject;
    }

    // Getter and Setter methods for teacherUsername
    public static String getTeacherUsername() {
        return teacherUsername;
    }

    public static void setTeacherUsername(String username) {
        teacherUsername = username;
    }
}
