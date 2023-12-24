package com.example.Current_Variables;

import com.example.klassroom.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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


    public static void fetchClassroomDetails(String classroomcode) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            DatabaseConnection.establishConnection();
            String sql = "SELECT * FROM classrooms WHERE classroom_code = ?";
            classroomCode = classroomcode;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, classroomCode);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Populate the fields with the retrieved data
                        classroomId = resultSet.getInt("classroom_id");
                        subjectName = resultSet.getString("subject_name");
                        teacherUsername = resultSet.getString("teacher_username");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
