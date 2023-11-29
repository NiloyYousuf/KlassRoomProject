package com.example.Notification;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class NotificationController {

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
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(new File( "src/main/resources/com/example/klassroom/classroomStudent.fxml").toURL());
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
        }).start();
    }
}