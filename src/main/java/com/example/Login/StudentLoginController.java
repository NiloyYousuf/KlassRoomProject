package com.example.Login;

import com.example.Notification.AssignmentNotificationChecker;
import com.example.Notification.CommentNotificationChecker;
import com.example.Notification.NotificationChecker;
import com.example.student.CurrentStudent;
import com.example.Current_Variables.Current_User;
import com.example.klassroom.DatabaseConnection;
import com.example.student.StudentMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentLoginController {
    @FXML
    private Button studentloginbutton;

    @FXML
    private TextArea studentIDTextArea;

    @FXML
    private PasswordField passwordTextArea;

    @FXML
    private Text errorMessageText;

    @FXML
    private void handleStudentLoginButtonClick() {
        // Get the student ID and password from the text areas
        String student_username = studentIDTextArea.getText();
        String password = passwordTextArea.getText();

        //System.out.println(StudentMenuController.student_username);

        // Fetch student information from the database using the provided DatabaseConnection class
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            try {
                // Define the SQL query to retrieve student information based on ID and password
                DatabaseConnection.establishConnection();
                String sql = "SELECT * FROM students WHERE student_username = ? AND student_password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, student_username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Student login successful
                    // You can proceed to the student's dashboard or perform other actions
                    errorMessageText.setText("Login successful"); // Clear any previous error message
                    Current_User.is_student=true;
                    CurrentStudent.CurrentStudentUsername=student_username;
                    FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/StudentFinalDashboard.fxml").toURL());
                    Parent studentLogin = loader.load();
                    // Start the notification checker for the logged-in student // for post notification
                    NotificationChecker notificationChecker = new NotificationChecker();
                    Thread notificationThread = new Thread(() -> notificationChecker.startCheckingNotifications());
                    notificationThread.start();

                    //for comment notification
                    CommentNotificationChecker commentNotificationChecker = new CommentNotificationChecker();
                    Thread commentnotificationThread = new Thread(() -> commentNotificationChecker.startCheckingCommentNotifications());
                    commentnotificationThread.start();


                    AssignmentNotificationChecker assignmentNotificationChecker = new AssignmentNotificationChecker();
                    Thread assignmentNotificationThread = new Thread(() -> assignmentNotificationChecker.startCheckingNotifications());
                    assignmentNotificationThread.start();



                    // Get the current scene and set the student login content
                    Scene currentScene = studentloginbutton.getScene();
                    currentScene.setRoot(studentLogin);

                } else {
                    // Student login failed
                    errorMessageText.setText("Invalid student ID or password");
                }


            } catch (SQLException e) {
                e.printStackTrace();
                errorMessageText.setText("Database error");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorMessageText.setText("Failed to obtain a database connection.");
        }
    }

    public  void Back_button_clicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(new File ("src/main/resources/com/example/Login/LoginPage.fxml").toURL());
        Parent post = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene =studentIDTextArea.getScene();
        currentScene.setRoot(post);
    }



}
