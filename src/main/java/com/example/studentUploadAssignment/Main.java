package com.example.studentUploadAssignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(new File( "src/main/resources/com/example/klassroom/studentUploadAssignment/studentUploadAssignment.fxml").toURI().toURL());
        Parent root = loader.load();

        // Set the FXML controller
        StudentUploadAssignmentController controller = loader.getController();

        // Create the main scene
        Scene scene = new Scene(root);

        // Set the stage
        primaryStage.setTitle("Student Assignment Upload");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
