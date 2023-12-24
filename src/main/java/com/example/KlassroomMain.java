package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

import static com.example.student.classroomStudentController.stopScheduler;

public class KlassroomMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new File("src/main/resources/com/example/Login/LoginPage.fxml").toURL());
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Hello!");
            primaryStage.setScene(scene);
            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                event.consume(); // Consumes the event to prevent the default close action

                Platform.runLater(() -> {
                    // Open a new JavaFX window or perform other operations
                    // Example: Opening another FXML file
                    try {
                        primaryStage.close(); // Close the current window

                        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Notification/notification.fxml").toURL());
                        Scene anotherScene = new Scene(loader.load());
                        Stage anotherStage = new Stage();
                        anotherStage.setScene(anotherScene);
                       anotherStage.initStyle(StageStyle.UNDECORATED);
                        anotherStage.initStyle(StageStyle.UTILITY);

                        anotherStage.show();
                        anotherStage.maxWidthProperty().setValue(1);
                        anotherStage.maxHeightProperty().setValue(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();

    }
}
