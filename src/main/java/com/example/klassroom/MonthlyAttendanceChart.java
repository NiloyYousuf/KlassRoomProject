package com.example.klassroom;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Map;
import java.util.HashMap;

public class MonthlyAttendanceChart extends JPanel {

    private int year;
    private int month;

    public MonthlyAttendanceChart(int year, int month) {
        this.year = year;
        this.month = month;

        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        add(chartPanel);


        JFrame frame = new JFrame("Monthly Employee Attendance Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);


    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Fetch monthly attendance data from the database
        Map<String, Integer> attendanceData = fetchMonthlyAttendanceData();
        for (Map.Entry<String, Integer> entry : attendanceData.entrySet()) {
            dataset.addValue(entry.getValue(), "Attendance", entry.getKey());
        }
        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "Monthly Employee Attendance",
                "Employee",
                "Attendance",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    public void generateMonthlyAttendanceChart() {
        JFrame frame = new JFrame("Monthly Employee Attendance Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
    }

    private Map<String, Integer> fetchMonthlyAttendanceData() {
        Map<String, Integer> attendanceData = new HashMap<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT employee_id, COUNT(*) AS attendance_count FROM attendance WHERE YEAR(date) = ? AND MONTH(date) = ? GROUP BY employee_id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int employeeId = resultSet.getInt("employee_id");
                int attendanceCount = resultSet.getInt("attendance_count");
                attendanceData.put("Employee " + employeeId, attendanceCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceData;
    }
}
