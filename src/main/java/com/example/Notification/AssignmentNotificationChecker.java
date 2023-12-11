package com.example.Notification;

import com.example.Current_Variables.CurrentClassroom;
import com.example.student.CurrentStudent;
import com.example.klassroom.DatabaseConnection;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Screen;

import java.io.IOException;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class AssignmentNotificationChecker {

    public void startCheckingNotifications() {
        // Create a separate thread to check for notifications every 10 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                checkNotifications();
            }
        }, 0, 15000); // Check for notifications every 10 seconds
    }

    private void checkNotifications() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // Query to check for notifications for the current student
            String query = "SELECT * FROM assignment_notification WHERE student_username = ?";
            DatabaseConnection.establishConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, CurrentStudent.CurrentStudentUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Display the notification message using the AssignmentNotificationController
                String notificationText = resultSet.getString("notification_text");
                showAssignmentNotification(notificationText);

                // Once the notification is shown, delete it from the database
                int notificationId = resultSet.getInt("notification_id");
                String classroom_code = resultSet.getString("classroom_code");
                CurrentClassroom.fetchClassroomDetails(classroom_code);
                deleteNotification(notificationId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteNotification(int notificationId) throws SQLException {
        // Delete the notification from the database
        Connection connection=DatabaseConnection.getConnection();
        DatabaseConnection.establishConnection();
        String deleteQuery = "DELETE FROM assignment_notification WHERE notification_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, notificationId);
        preparedStatement.executeUpdate();
    }

    private void showAssignmentNotification(String notificationText) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignmentNotificationShown.fxml"));
                Parent notificationRoot = loader.load();

                AssignmentNotificationController controller = loader.getController();
                controller.setNotificationText(notificationText);

                Stage notificationStage = new Stage();
                notificationStage.initStyle(StageStyle.UNDECORATED);
                notificationStage.setScene(new Scene(notificationRoot));

                double x = Screen.getPrimary().getVisualBounds().getMaxX() - notificationRoot.prefWidth(-1) - 20;
                double y = Screen.getPrimary().getVisualBounds().getMaxY() - notificationRoot.prefHeight(-1) - 60;
                notificationStage.setX(x);
                notificationStage.setY(y);

                notificationStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
