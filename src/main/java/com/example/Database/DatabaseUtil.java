package com.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtil {
    // Add foreign key constraint to an existing table
    public static void addForeignKeyConstraint(Connection connection, String tableName, String columnName, String foreignTableName, String foreignColumnName) throws SQLException {
        String sql = "ALTER TABLE " + tableName +
                " ADD CONSTRAINT fk_" + columnName +
                " FOREIGN KEY (" + columnName + ")" +
                " REFERENCES " + foreignTableName + "(" + foreignColumnName + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
            System.out.println("Foreign key constraint added successfully.");
        }
    }
}
