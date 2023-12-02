package com.example.Notification;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AssignmentNotificationController {

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

    // Add any other actions you want to perform when the notification is clicked or closed
}
