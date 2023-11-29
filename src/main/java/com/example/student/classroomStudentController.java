package com.example.student;

import com.example.klassroom.ClassroomPostsDAO;
import com.example.Current_Variables.CurrentClassroom;
import com.example.klassroom.GlobalFxmlString;
import com.example.klassroom.Post;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class classroomStudentController implements Initializable {

    @FXML
    private TextField studentNameTextField;

    @FXML
    private TextField courseCodeTextField;

    @FXML
    private TextField subjectNameTextField;

    @FXML
    private ScrollPane scrollPane; // ScrollPane to make content scrollable

    @FXML
    private VBox postContainer; // VBox to hold post tiles

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            studentNameTextField.setText(StudentMenuController.student_username);
            courseCodeTextField.setText(CurrentClassroom.classroomCode);
            subjectNameTextField.setText(CurrentClassroom.getSubjectName());
            System.out.println(CurrentClassroom.getClassroomId());

            List<Post> posts = ClassroomPostsDAO.getPostsByClassroom(CurrentClassroom.getClassroomId());

            postContainer.getChildren().clear();
            scheduler.scheduleAtFixedRate(this::refreshPostContainer, 0, 5, TimeUnit.SECONDS);

            for (Post post : posts) {
                loadPostTile(post);
            }
        });
    }

    private void refreshPostContainer() {
        List<Post> posts = ClassroomPostsDAO.getPostsByClassroom(CurrentClassroom.getClassroomId());

        Platform.runLater(() -> {
            postContainer.getChildren().clear();

            for (Post post : posts) {
                loadPostTile(post);
            }

            // Refresh the scene
            Scene scene = postContainer.getScene();
            if (scene != null) {
                scene.getRoot().requestLayout();
            }

          //  System.out.println("Posts refreshed");
        });
    }





    private void loadPostTile(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Post/Student_post_tile.fxml"));
          //  System.out.println("Loading FXML file...");
            AnchorPane postTile = loader.load();
            //System.out.println("FXML file loaded successfully!");
            StudentPostTileController postTileController = loader.getController();
            postTileController.setPostData(post);
            postContainer.getChildren().add(postTile);
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception details
            throw new RuntimeException(e);
        }
    }




    public static void stopScheduler() {
        scheduler.shutdown();
    }

    public void Go_back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentFinalDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) studentNameTextField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void view_attendance_clicked() throws IOException {
        GlobalFxmlString.FXML_to_load = "src/main/resources/com/example/Attendance/Student Attendance.fxml";
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/StudentFinalDashboard.fxml").toURL());
        Parent root = loader.load();
        Stage stage = (Stage) studentNameTextField.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
