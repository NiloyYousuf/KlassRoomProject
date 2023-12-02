package com.example.Notification;

import com.example.Current_Variables.Current_User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class CommentNotificationController {

    @FXML
    private Label notificationLabel;

    public void setNotificationText(String text) {
        notificationLabel.setText(text);
    }

    @FXML
    private void closeNotification() {
        Stage stage = (Stage) notificationLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openFXML() {
        closeNotification(); // Close the notification window after navigating to the post

        if (Current_User.is_student==true) {
            // If the current user is a student, open StudentPost.fxml
            openFXML("src/main/resources/com/example/Post/StudentPost.fxml");
        } else {
            // If the current user is a teacher, open TeacherPost.fxml
            openFXML("src/main/resources/com/example/Post/TeacherPost.fxml");
        }
    }

    private void openFXML(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(new File(fxmlFileName).toURL());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
