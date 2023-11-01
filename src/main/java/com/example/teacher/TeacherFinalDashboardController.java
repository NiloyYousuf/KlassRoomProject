package com.example.teacher;
import com.example.klassroom.GlobalFxmlString;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TeacherFinalDashboardController implements Initializable {

    @FXML
    private AnchorPane opacityPane,drawerPane;
    @FXML
    private  AnchorPane containerPane;

    @FXML
    private Label drawerImage;

    @FXML
    private ImageView exit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        exit.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                GlobalFxmlString.FXML_to_load=null;
                FXMLLoader loader = null;
                try {
                    loader = new FXMLLoader(new File("src/main/resources/com/example/Login/LoginPage.fxml").toURL());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Get the current stage (assuming you have a reference to the current stage)
                Stage stage = (Stage) exit.getScene().getWindow();

                // Set the new FXML content on the current stage
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                // If 'Yes' is pressed, navigate to another FXML file
                // For example:
                // loadAnotherFXML();
            }
        });

        opacityPane.setVisible(false);

        FadeTransition fadeTransition=new FadeTransition(Duration.seconds(0.5),opacityPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImage.setOnMouseClicked(event -> {
            opacityPane.setVisible(true);
            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.15);
            fadeTransition1.play();

            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),drawerPane);
            translateTransition1.setByX(+600);
            translateTransition1.play();
        });

        opacityPane.setOnMouseClicked(event -> {



            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0.15);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> {
                opacityPane.setVisible(false);
            });


            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),drawerPane);
            translateTransition1.setByX(-600);
            translateTransition1.play();
        });
        loadMyContent();
    }



    @FXML
    private void loadMyContent() {

        try {
            if(GlobalFxmlString.FXML_to_load!= null) {
                FXMLLoader loader = new FXMLLoader(new File(GlobalFxmlString.FXML_to_load).toURL());
                Parent content = loader.load();
                containerPane.getChildren().clear(); // Clear existing content (if any)
                containerPane.getChildren().add(content);
            }
            else
            {
                FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/klassroom/TeacherMenu.fxml").toURL());
                Parent content = loader.load();
                containerPane.getChildren().clear(); // Clear existing content (if any)
                containerPane.getChildren().add(content);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
        }
    }



    public void load_classrooms_page()
    {
        try {
            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/klassroom/TeacherMenu.fxml").toURL());
            Parent content = loader.load();
            containerPane.getChildren().clear(); // Clear existing content (if any)
            containerPane.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
        }
    }




    public void load_create_classroom()
    {
        try {
            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/klassroom/Createclassroom.fxml").toURL());
            Parent content = loader.load();
            containerPane.getChildren().clear(); // Clear existing content (if any)
            containerPane.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
        }

    }


    public void load_view_assignments()
    {
        try {
            FXMLLoader loader = new FXMLLoader(new File( "src/main/resources/com/example/teacherassignment/TeacherFetchAssignment.fxml").toURI().toURL());
            Parent content = loader.load();
            containerPane.getChildren().clear(); // Clear existing content (if any)
            containerPane.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
        }

    }


    public void load_Info_page()
    {

        try {
            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/klassroom/teacher_profile.fxml").toURL());
            Parent content = loader.load();
            containerPane.getChildren().clear(); // Clear existing content (if any)
            containerPane.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential exceptions here
        }

    }



}