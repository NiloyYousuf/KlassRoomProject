package com.example.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class NotificationTrigger {
    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();

            // Drop the trigger if it already exists
            String dropTriggerSQL = "DROP TRIGGER IF EXISTS post_notification_trigger";
            statement.executeUpdate(dropTriggerSQL);

            // Create the trigger
            String triggerSQL = "CREATE TRIGGER post_notification_trigger AFTER INSERT ON posts " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "DECLARE student_username_val VARCHAR(50); " +
                    "DECLARE classroom_code_val VARCHAR(10); " +
                    "SELECT csj.student_username, c.classroom_code " +
                    "FROM classroom_student_junction csj " +
                    "JOIN classrooms c ON c.classroom_id = NEW.classroom_id " +
                    "WHERE csj.classroom_code = c.classroom_code " +
                    "INTO student_username_val, classroom_code_val; " +
                    "INSERT INTO notifications (classroom_code, student_username, notification_text) " +
                    "VALUES (classroom_code_val, student_username_val, CONCAT('New post made for classroom: ', classroom_code_val)); " +
                    "END;";

            statement.executeUpdate(triggerSQL);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

