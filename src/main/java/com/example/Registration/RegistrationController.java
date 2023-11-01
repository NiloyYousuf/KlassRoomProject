package com.example.Registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class RegistrationController {

    @FXML
    public void handleStudentRegistration(ActionEvent event) {
        // Handle redirection to the student registration page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student_register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleTeacherRegistration(ActionEvent event) {
        // Handle redirection to the teacher registration page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backbuttonRegistration(ActionEvent event) {
        // Handle redirection to the teacher registration page
        try {
            FXMLLoader loader = new FXMLLoader(new File( "src/main/resources/com/example/Login/LoginPage.fxml").toURI().toURL());
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
