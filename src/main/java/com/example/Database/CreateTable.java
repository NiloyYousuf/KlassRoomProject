package com.example.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public static void main(String[] args) {
        // Database connection information
        String url = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12666169";
        String user = "sql12666169";
        String password = "dZmh6WCGlT";

        // Create the tables
        createAssignmentsTable(url, user, password);
        createAttendanceTable(url, user, password);
        createClassroomStudentJunctionTable(url, user, password);
        createClassroomsTable(url, user, password);
        createCommentsTable(url, user, password);
        createPostsTable(url, user, password);
        createStudentAssignmentJunctionTable(url, user, password);
        createStudentsTable(url, user, password);
        createTeachersTable(url, user, password);
        createNotificationsTable(url, user,  password);
    }

    public static void createTable(String url, String user, String password, String tableName, String tableDefinition) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE " + tableName + " (" + tableDefinition + ")";
            statement.executeUpdate(sql);
            System.out.println("Table " + tableName + " created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createAssignmentsTable(String url, String user, String password) {
        String tableName = "assignments";
        String tableDefinition = "`Assignment_ID` int NOT NULL AUTO_INCREMENT, " +
                "`Assignment_Text` text, " +
                "`Assign_Date` date DEFAULT NULL, " +
                "`Deadline` date DEFAULT NULL, " +
                "`Attachment` longblob, " +
                "`Marks` int DEFAULT NULL, " +
                "`Teacher_Username` varchar(50) DEFAULT NULL, " +
                "`Classroom_Code` varchar(10) DEFAULT NULL, " +
                "PRIMARY KEY (`Assignment_ID`), " +
                "KEY `Teacher_Username` (`Teacher_Username`), " +
                "KEY `Classroom_Code` (`Classroom_Code`), " +
                "CONSTRAINT `assignments_ibfk_1` FOREIGN KEY (`Teacher_Username`) REFERENCES `teachers` (`teacher_username`), " +
                "CONSTRAINT `assignments_ibfk_2` FOREIGN KEY (`Classroom_Code`) REFERENCES `classrooms` (`classroom_code`)";
        createTable(url, user, password, tableName, tableDefinition);
    }

    public static void createAttendanceTable(String url, String user, String password) {
        String tableName = "attendance";
        String tableDefinition = "`student_username` varchar(50) NOT NULL, " +
                "`Date` date NOT NULL, " +
                "`classroom_code` varchar(10) NOT NULL, " +
                "`is_present` tinyint(1) DEFAULT NULL, " +
                "PRIMARY KEY (`student_username`,`Date`,`classroom_code`), " +
                "KEY `classroom_code` (`classroom_code`), " +
                "CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`student_username`) REFERENCES `students` (`student_username`), " +
                "CONSTRAINT `attendance_ibfk_2` FOREIGN KEY (`classroom_code`) REFERENCES `classrooms` (`classroom_code`)";
        createTable(url, user, password, tableName, tableDefinition);
    }

    public static void createClassroomStudentJunctionTable(String url, String user, String password) {
        String tableName = "classroom_student_junction";
        String tableDefinition = "`classroom_code` varchar(10) NOT NULL, " +
                "`student_username` varchar(50) NOT NULL, " +
                "PRIMARY KEY (`classroom_code`,`student_username`), " +
                "KEY `student_username` (`student_username`), " +
                "CONSTRAINT `classroom_student_junction_ibfk_1` FOREIGN KEY (`classroom_code`) REFERENCES `classrooms` (`classroom_code`), " +
                "CONSTRAINT `classroom_student_junction_ibfk_2` FOREIGN KEY (`student_username`) REFERENCES `students` (`student_username`)";
        createTable(url, user, password, tableName, tableDefinition);
    }

    public static void createClassroomsTable(String url, String user, String password) {
        String tableName = "classrooms";
        String tableDefinition = "`classroom_id` int NOT NULL AUTO_INCREMENT, " +
                "`teacher_username` varchar(50) DEFAULT NULL, " +
                "`classroom_code` varchar(10) NOT NULL, " +
                "`classroom_password` varchar(10) NOT NULL, " +
                "`subject_name` varchar(50) NOT NULL, " +
                "PRIMARY KEY (`classroom_id`), " +
                "UNIQUE KEY `idx_classroom_code` (`classroom_code`), " +
                "KEY `teacher_username` (`teacher_username`), " +
                "CONSTRAINT `classrooms_ibfk_1` FOREIGN KEY (`teacher_username`) REFERENCES `teachers` (`teacher_username`)";
        createTable(url, user, password, tableName, tableDefinition);
    }

    public static void createCommentsTable(String url, String user, String password) {
        String tableName = "comments";
        String tableDefinition = "`commentID` int NOT NULL AUTO_INCREMENT, " +
                "`commentText` text NOT NULL, " +
                "`commentTime` time NOT NULL, " +
                "`CommentDate` date NOT NULL, " +
                "`postID` int NOT NULL, " +
                "`student_username` varchar(50) DEFAULT NULL, " +
                "`teacher_username` varchar(50) DEFAULT NULL, " +
                "PRIMARY KEY (`commentID`), " +
                "KEY `postID` (`postID`), " +
                "KEY `student_username` (`student_username`), " +
                "KEY `teacher_username` (`teacher_username`), " +
                "CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`postID`) REFERENCES `posts` (`post_id`), " +
                "CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`student_username`) REFERENCES `students` (`student_username`), " +
                "CONSTRAINT `comments_ibfk_3` FOREIGN KEY (`teacher_username`) REFERENCES `teachers` (`teacher_username`)";
        createTable(url, user, password, tableName, tableDefinition);
    }

    public static void createPostsTable(String url, String user, String password) {
        String tableName = "posts";
        String tableDefinition = "`post_id` int NOT NULL AUTO_INCREMENT, " +
                "`classroom_id` int NOT NULL, " +
                "`post_time` varchar(50) NOT NULL, " +
                "`post_date` varchar(50) NOT NULL, " +
                "`post_text` text NOT NULL, " +
                "`attachment` longblob, " +
                "`original_filename` varchar(255) DEFAULT NULL, " +
                "PRIMARY KEY (`post_id`), " +
                "KEY `classroom_id` (`classroom_id`), " +
                "CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`classroom_id`) REFERENCES `classrooms` (`classroom_id`) ON DELETE CASCADE";
        createTable(url, user, password, tableName, tableDefinition);
    }

    public static void createStudentAssignmentJunctionTable(String url, String user, String password) {
        String tableName = "student_assignment_junction";
        String tableDefinition = "`Assignment_ID` int NOT NULL, " +
                "`Student_Username` varchar(50) NOT NULL, " +
                "`Submission_Date` date DEFAULT NULL, " +
                "`Submission_Status` varchar(50) DEFAULT NULL, " +
                "`Marks_Obtained` int DEFAULT NULL, " +
                "`Uploaded_Assignment` longblob, " +
                "`Original_Filename` varchar(255) DEFAULT NULL, " +
                "PRIMARY KEY (`Assignment_ID`,`Student_Username`), " +
                "KEY `Student_Username` (`Student_Username`), " +
                "CONSTRAINT `student_assignment_junction_ibfk_1` FOREIGN KEY (`Assignment_ID`) REFERENCES `assignments` (`Assignment_ID`), " +
                "CONSTRAINT `student_assignment_junction_ibfk_2` FOREIGN KEY (`Student_Username`) REFERENCES `students` (`student_username`)";
        createTable(url, user, password, tableName, tableDefinition);
    }

    public static void createStudentsTable(String url, String user, String password) {
        String tableName = "students";
        String tableDefinition = "`student_username` varchar(50) NOT NULL, " +
                "`student_password` varchar(255) NOT NULL, " +
                "`student_email` varchar(255) DEFAULT NULL, " +
                "`photo` longblob, " +
                "PRIMARY KEY (`student_username`), " +
                "UNIQUE KEY `student_username` (`student_username`)";
        createTable(url, user, password, tableName, tableDefinition);
    }

    public static void createTeachersTable(String url, String user, String password) {
        String tableName = "teachers";
        String tableDefinition = "`teacher_username` varchar(50) NOT NULL, " +
                "`teacher_password` varchar(255) NOT NULL, " +
                "`teacher_email` varchar(255) DEFAULT NULL, " +
                "`photo` longblob, " +
                "PRIMARY KEY (`teacher_username`), " +
                "UNIQUE KEY `teacher_username` (`teacher_username`)";
        createTable(url, user, password, tableName, tableDefinition);
    }

    public static void createNotificationsTable(String url, String user, String password) {
        String tableName = "notifications";
        String tableDefinition = "`notification_id` int NOT NULL AUTO_INCREMENT, " +
                "`classroom_code` varchar(10) DEFAULT NULL, " +
                "`student_username` varchar(50) DEFAULT NULL, " +
                "`notification_text` text, " +
                "PRIMARY KEY (`notification_id`)";
        createTable(url, user, password, tableName, tableDefinition);
    }

}
