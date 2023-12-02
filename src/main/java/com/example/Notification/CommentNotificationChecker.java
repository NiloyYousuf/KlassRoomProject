package com.example.Notification;

import com.example.Current_Variables.CurrentClassroom;
import com.example.Current_Variables.Current_User;
import com.example.Current_Variables.currentPost;
import com.example.student.CurrentStudent;
import com.example.klassroom.DatabaseConnection;
import com.example.teacher.CurrentTeacher;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class CommentNotificationChecker {

    public void startCheckingCommentNotifications() {
        // Create a separate thread to check for comment notifications every 10 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                checkCommentNotifications();
            }
        }, 0, 20000); // Check for comment notifications every 10 seconds
    }

    private void checkCommentNotifications() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // Query to check for comment notifications for the current student
            String query = "SELECT * FROM comment_notification WHERE username = ?";
            DatabaseConnection.establishConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if(Current_User.is_student==true)
            {preparedStatement.setString(1, CurrentStudent.CurrentStudentUsername);}
            else {
                preparedStatement.setString(1, CurrentTeacher.current_teacher_username);
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Display the comment notification message
                String notificationText = resultSet.getString("notification_text");
                int postID=resultSet.getInt("post_id");
                currentPost.currentPostId=postID;
                showCommentNotification(notificationText);
                System.out.println(notificationText);

                // Once the comment notification is shown, delete it from the database
                int notificationId = resultSet.getInt("notification_id");
                deleteCommentNotification(notificationId, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCommentNotification(int notificationId, Connection connection) throws SQLException {
        // Delete the comment notification from the database
        DatabaseConnection.establishConnection();
        String deleteQuery = "DELETE FROM comment_notification WHERE notification_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, notificationId);
        preparedStatement.executeUpdate();
    }

    private void showCommentNotification(String notificationText) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CommentNotificationShown.fxml"));
                Parent commentNotificationRoot = loader.load();

                CommentNotificationController controller = loader.getController();
                controller.setNotificationText(notificationText);

                Stage commentNotificationStage = new Stage();
                commentNotificationStage.initStyle(StageStyle.UNDECORATED);
                commentNotificationStage.setScene(new Scene(commentNotificationRoot));

                double x = Screen.getPrimary().getVisualBounds().getMaxX() - commentNotificationRoot.prefWidth(-1) - 20;
                double y = Screen.getPrimary().getVisualBounds().getMaxY() - commentNotificationRoot.prefHeight(-1) - 60;
                commentNotificationStage.setX(x);
                commentNotificationStage.setY(y);

                commentNotificationStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
