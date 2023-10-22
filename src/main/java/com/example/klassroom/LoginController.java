package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button studentButton;

    @FXML
    private Button teacherButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize any necessary code here
    }
    @FXML
    private AnchorPane content;


    @FXML
    private void loginAsStudent() {
        try {
            // Load the student login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("studentlogin.fxml"));//was changed in the fxml file
            Parent studentLogin = loader.load();

            // Get the current scene and set the student login content
            Scene currentScene = studentButton.getScene();
            currentScene.setRoot(studentLogin);
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions that may occur during loading
        }
    }
    @FXML
    private void loginAsTeacher() {
        try {
            // Load the student login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacherlogin.fxml"));// was changed in the fxml file
            Parent studentLogin = loader.load();

            // Get the current scene and set the student login content
            Scene currentScene =teacherButton.getScene();
            currentScene.setRoot(studentLogin);
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions that may occur during loading
        }
    }



}
