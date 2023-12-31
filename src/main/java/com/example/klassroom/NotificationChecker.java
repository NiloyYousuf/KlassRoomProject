package com.example.klassroom;
import com.example.student.CurrentStudent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class NotificationChecker {

    public void startCheckingNotifications() {
        // Create a separate thread to check for notifications every 10 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                checkNotifications();
            }
        }, 0, 10000); // Check for notifications every 10 seconds
    }

    private void checkNotifications() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // Query to check for notifications for the current student
            DatabaseConnection.establishConnection();
            String query = "SELECT * FROM notifications WHERE student_username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, CurrentStudent.CurrentStudentUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Display the notification message using Swing's JOptionPane
                String notificationText = resultSet.getString("notification_text");
                showNotification(notificationText);
                System.out.println(notificationText);

                // Once the notification is shown, delete it from the database
                int notificationId = resultSet.getInt("notification_id");
                deleteNotification(notificationId, connection);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteNotification(int notificationId, Connection connection) throws SQLException {
        // Delete the notification from the database
        DatabaseConnection.establishConnection();
        String deleteQuery = "DELETE FROM notifications WHERE notification_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, notificationId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private void showNotification(String notificationText) {
        Thread notificationDisplayThread = new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                JWindow notificationWindow = new JWindow();
                JPanel notificationPanel = new JPanel();
                notificationWindow.addWindowFocusListener(new WindowAdapter() {
                    @Override
                    public void windowGainedFocus(WindowEvent e) {
                        notificationWindow.toFront(); // Bring the window to the front when focused
                    }
                });
                notificationPanel.setLayout(new BorderLayout());
                notificationPanel.setBackground(Color.BLACK);
                notificationPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                notificationPanel.setPreferredSize(new Dimension(400, 120));

                JLabel notificationLabel = new JLabel(notificationText);
                notificationLabel.setForeground(Color.WHITE);
                notificationPanel.add(notificationLabel, BorderLayout.CENTER);

                JButton closeButton = new JButton("X");
                closeButton.setForeground(Color.WHITE);
                closeButton.setBackground(Color.BLACK);
                closeButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                closeButton.setPreferredSize(new Dimension(20, 20));
                closeButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        notificationWindow.dispose();
                    }
                });
                notificationPanel.add(closeButton, BorderLayout.LINE_END);

                JButton openFXMLButton = new JButton("Open FXML");
                openFXMLButton.setForeground(Color.WHITE);
                openFXMLButton.setBackground(Color.BLACK);
                openFXMLButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                openFXMLButton.setPreferredSize(new Dimension(100, 20));
                openFXMLButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        new Thread(() -> {
                            Platform.runLater(() -> {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassroomStudent.fxml"));
                                    Parent root = loader.load();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root));
                                    stage.show();
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                }
                            });
                        }).start();
                        // Launches the JavaFX Application to open the FXML file
                    }
                });
                notificationPanel.add(openFXMLButton, BorderLayout.PAGE_END);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int x = screenSize.width - notificationPanel.getPreferredSize().width - 20;
                int y = screenSize.height - notificationPanel.getPreferredSize().height - 60;
                notificationWindow.setLocation(x, y);
                notificationWindow.toFront();
                notificationWindow.add(notificationPanel);
                notificationWindow.pack();
                notificationWindow.setShape(new RoundRectangle2D.Double(0, 0, notificationPanel.getPreferredSize().width, notificationPanel.getPreferredSize().height, 10, 10));
                notificationWindow.setVisible(true);
            });
        });

        notificationDisplayThread.setDaemon(true); // Set the thread as a daemon thread
        notificationDisplayThread.start();
    }
    // Then call openNewJavaFXStage method when needed

}
