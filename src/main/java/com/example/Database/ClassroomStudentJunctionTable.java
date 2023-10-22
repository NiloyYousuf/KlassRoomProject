package com.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// something was changed here
public class ClassroomStudentJunctionTable {

    public static void main(String[] args) {
        try {
            Connection connection =DatabaseConnection.getConnection();
            // Delete the existing classroom_student_junction table (if it exists)
          //  deleteClassroomStudentJunctionTable(connection);

            // Create the classroom_student_junction table with both attributes as primary keys
           createClassroomStudentJunctionTable(connection);
//            insertDataIntoClassroomStudentJunction(connection, "C101", "student1");
//            insertDataIntoClassroomStudentJunction(connection, "C101", "student2");
//            insertDataIntoClassroomStudentJunction(connection, "C102", "student1");
           displayAllDataInClassroomStudentJunction(connection);

            // Close the database connection
          //  connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createClassroomStudentJunctionTable(Connection connection) throws SQLException {
        // Define the table creation SQL with both attributes as primary keys
        String createTableSQL = "CREATE TABLE classroom_student_junction ("
                + "classroom_code VARCHAR(10), "
                + "student_username VARCHAR(50), "
                + "PRIMARY KEY (classroom_code, student_username)"
                + ")";

        // Execute the SQL to create the table
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.execute();
            System.out.println("classroom_student_junction table created successfully.");
        }

        // Add foreign key constraints
        DatabaseUtil.addForeignKeyConstraint(connection, "classroom_student_junction", "classroom_code", "classrooms", "classroom_code");
        DatabaseUtil.addForeignKeyConstraint(connection, "classroom_student_junction", "student_username", "students", "student_username");
    }

    private static void deleteClassroomStudentJunctionTable(Connection connection) throws SQLException {
        // Define the SQL to delete the table if it exists
        String deleteTableSQL = "DROP TABLE IF EXISTS classroom_student_junction";

        // Execute the SQL to delete the table
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteTableSQL)) {
            preparedStatement.execute();
            System.out.println("classroom_student_junction table deleted successfully (if it existed).");
        }
    }



    private static void insertDataIntoClassroomStudentJunction(Connection connection, String classroomCode, String studentUsername) throws SQLException {
        // Define the SQL to insert data into the table
        String insertDataSQL = "INSERT INTO classroom_student_junction (classroom_code, student_username) VALUES (?, ?)";

        // Execute the SQL to insert data
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {
            preparedStatement.setString(1, classroomCode);
            preparedStatement.setString(2, studentUsername);
            preparedStatement.executeUpdate();
            System.out.println("Data inserted into classroom_student_junction table.");
        }
    }
    private static void displayAllDataInClassroomStudentJunction(Connection connection) throws SQLException {
        String selectDataSQL = "SELECT * FROM classroom_student_junction";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectDataSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println("Data in classroom_student_junction table:");

            while (resultSet.next()) {
                String classroomCode = resultSet.getString("classroom_code");
                String studentUsername = resultSet.getString("student_username");

                System.out.println("Classroom Code: " + classroomCode + ", Student Username: " + studentUsername);
            }
        }
    }


}
