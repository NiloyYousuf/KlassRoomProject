package com.example.teacher;

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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ClassroomTeacherController implements Initializable {

    @FXML
    private TextField teacherNameTextField;

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
            teacherNameTextField.setText(TeacherMenuController.teacher_username);
            courseCodeTextField.setText(CurrentClassroom.classroomCode);
            subjectNameTextField.setText(CurrentClassroom.getSubjectName());
           // System.out.println(CurrentClassroom.getClassroomId());
           // ClassroomPostsDAO.printAllPosts(CurrentClassroom.getClassroomId());
            List<Post> posts = ClassroomPostsDAO.getPostsByClassroom(CurrentClassroom.getClassroomId());
           // scheduler.scheduleAtFixedRate(this::refreshPostContainer, 0, 5, TimeUnit.SECONDS);
            // Clear existing items in the postContainer
            postContainer.getChildren().clear();

            // Populate the VBox with post tiles
            for (Post post : posts) {
                FXMLLoader loader = null;
                try {
                    loader = new FXMLLoader(new File("src/main/resources/com/example/Post/Teacher_post_tile.fxml").toURL());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                AnchorPane postTile;
                try {
                    postTile = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Set the post data in the Student_post_tile.fxml controller
                TeacherPostTileController postTileController = loader.getController();
                postTileController.setPostData(post); // Implement this method in your StudentPostTileController

                // Add the post tile to the VBox
               postContainer.getChildren().add(postTile);
            }
        });
    }


    @FXML
    private Button Make_post_button;


    @FXML
    private TextArea post;
    @FXML
    private void make_post_button_clicked() throws FileNotFoundException, MalformedURLException {
        if (post.getText().trim().isEmpty()) {
            // Show alert for empty post field
            showAlert("Empty Post Field", "Post field cannot be empty.");
            return; // Exit the method without making the post
        }

        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        if (selectedFile != null) {
            ClassroomPostsDAO.insertPost(CurrentClassroom.getClassroomId(), formattedTime, date.toString(), post.getText(), selectedFile, originalFilename);
        } else {
            ClassroomPostsDAO.insertPost(CurrentClassroom.getClassroomId(), formattedTime, date.toString(), post.getText(), null, originalFilename);
        }

        // Show alert for post success
        showAlert("Post Updated", "Post updated successfully!");

        // Clear post field and selected file
        post.clear();
        selectedFile = null;
        originalFilename = null;
        selectedFileLabel.setText(""); // Clear the label text

        refreshPostContainer();
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void refreshPostContainer() throws MalformedURLException {
        List<Post> posts = ClassroomPostsDAO.getPostsByClassroom(CurrentClassroom.getClassroomId());

        // Clear existing items in the postContainer
        postContainer.getChildren().clear();

        // Populate the VBox with post tiles
        for (Post post : posts) {
            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Post/Teacher_post_tile.fxml").toURL());
            AnchorPane postTile;
            try {
                postTile = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Set the post data in the Student_post_tile.fxml controller
            TeacherPostTileController postTileController = loader.getController();
            postTileController.setPostData(post); // Implement this method in your StudentPostTileController

            // Add the post tile to the VBox
            postContainer.getChildren().add(postTile);
        }
    }

@FXML
private  Button attendance;

    @FXML
    private  void  attendance_button_clicked() throws IOException {
        TeacherTakingStudentAttendanceListController.classroomCode=CurrentClassroom.classroomCode;
        //System.out.println(TeacherTakingStudentAttendanceListController.classroomCode);
        GlobalFxmlString.FXML_to_load="src/main/resources/com/example/Attendance/Teacher_taking_student_Attendance_list.fxml";
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
        Parent studentLogin = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene = attendance.getScene();
        currentScene.setRoot(studentLogin);
    }

    @FXML
    private  void  Post_assignment_button_clicked() throws IOException {
        TeacherTakingStudentAttendanceListController.classroomCode=CurrentClassroom.classroomCode;
      //  System.out.println(TeacherTakingStudentAttendanceListController.classroomCode);
        GlobalFxmlString.FXML_to_load="src/main/resources/com/example/Assignment/AssignmentPostForm.fxml";
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
        Parent studentLogin = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene = attendance.getScene();
        currentScene.setRoot(studentLogin);
    }



    @FXML
    private Label selectedFileLabel;

    private File selectedFile;
    private String originalFilename; // Add the original filename field

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            selectedFile = file;
            originalFilename = file.getName(); // Store the original filename
            selectedFileLabel.setText("Selected File: " + originalFilename);
        }
    }

    public void Go_back()
    {
        try {
            // Load the new FXML file

            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
            Parent root = loader.load();

            // Get the current stage (assuming you have a reference to the current stage)
            Stage stage = (Stage)teacherNameTextField.getScene().getWindow();

            // Set the new FXML content on the current stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopScheduler() {
        scheduler.shutdown();
    }

}
