package com.example.student;

import com.example.Current_Variables.CurrentClassroom;
import com.example.klassroom.DatabaseConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StudentMonthlyAttendanceController extends JPanel {

    private int year;
    private int month;

    public StudentMonthlyAttendanceController(int year, int month) {
        this.year = year;
        this.month = month;

        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        add(chartPanel);

        JFrame frame = new JFrame("Monthly Student Attendance Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Fetch monthly attendance data from the database grouped by student
        Map<String, Integer> attendanceData = fetchMonthlyAttendanceData();
        for (Map.Entry<String, Integer> entry : attendanceData.entrySet()) {
            dataset.addValue(entry.getValue(), "Attendance", entry.getKey());
        }
        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "Monthly Student Attendance",
                "Student",
                "Attendance",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    public void generateMonthlyAttendanceChart() {
        JFrame frame = new JFrame("Monthly Student Attendance Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
    }

    private Map<String, Integer> fetchMonthlyAttendanceData() {
        Map<String, Integer> attendanceData = new HashMap<>();
        Connection connection = DatabaseConnection.getConnection();
        try {
            String sql = "SELECT student_username, COUNT(*) AS attendance_count FROM attendance " +
                    "WHERE YEAR(Date) = ? AND MONTH(Date) = ? AND classroom_code = ?" +
                    "GROUP BY student_username";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);
            preparedStatement.setString(3, CurrentClassroom.classroomCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String studentUsername = resultSet.getString("student_username");
                int attendanceCount = resultSet.getInt("attendance_count");
                attendanceData.put(studentUsername, attendanceCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceData;
    }
}
