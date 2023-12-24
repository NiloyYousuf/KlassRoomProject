package com.example.Login;

import com.example.Notification.CommentNotificationChecker;
import com.example.teacher.CurrentTeacher;
import com.example.Current_Variables.Current_User;
import com.example.klassroom.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherLoginController {
    @FXML
    private Button teacherloginbutton;

    @FXML
    private TextField teacherIDTextArea;

    @FXML
    private PasswordField passwordTextArea;

    @FXML
    private Text errorMessageText;



    @FXML
    private void handleTeacherLoginButtonClick() {
        // Get the teacher ID and password from the text areas
        String teacher_username = teacherIDTextArea.getText();
        String password = passwordTextArea.getText();

        // Fetch teacher information from the database using the provided DatabaseConnection class
        Connection connection = DatabaseConnection.getConnection();
        DatabaseConnection.establishConnection();
            try {

                // Define the SQL query to retrieve teacher information based on ID and password
                String sql = "SELECT * FROM teachers WHERE teacher_username = ? AND teacher_password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, teacher_username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    errorMessageText.setText("Login successful"); // Clear any previous error message
                    CurrentTeacher.current_teacher_username=teacher_username;
                  //  System.out.println(CurrentTeacher.current_teacher_username);
                    Current_User.is_student=false;
                    FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
                    Parent studentLogin = loader.load();
                    // Get the current scene and set the student login content
                    Scene currentScene = teacherloginbutton.getScene();
                    currentScene.setRoot(studentLogin);

                    CommentNotificationChecker commentNotificationChecker = new CommentNotificationChecker();
                    Thread commentnotificationThread = new Thread(() -> commentNotificationChecker.startCheckingCommentNotifications());
                    commentnotificationThread.start();

                } else {
                    // Teacher login failed
                    errorMessageText.setText("Invalid teacher ID or password");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                errorMessageText.setText("Database error");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



    public  void Back_button_clicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Login/LoginPage.fxml").toURL());
        Parent post = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene =teacherIDTextArea.getScene();
        currentScene.setRoot(post);
    }

}
