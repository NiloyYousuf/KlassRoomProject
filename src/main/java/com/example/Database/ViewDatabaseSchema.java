package com.example.Database;

import com.example.klassroom.DatabaseConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewDatabaseSchema {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a database connection
            connection = DatabaseConnection.getConnection();

            // Get the database metadata
            DatabaseMetaData metaData = connection.getMetaData();

            // Retrieve a list of tables in the database
            ResultSet tablesResultSet = metaData.getTables("railway", null, null, new String[]{"TABLE"});

            while (tablesResultSet.next()) {
                String tableName = tablesResultSet.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);

                // Retrieve column information for each table
                ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null);

                while (columnsResultSet.next()) {
                    String columnName = columnsResultSet.getString("COLUMN_NAME");
                    String columnType = columnsResultSet.getString("TYPE_NAME");
                    int columnSize = columnsResultSet.getInt("COLUMN_SIZE");

                    String columnInfo = columnName + " (" + columnType + ", Size: " + columnSize;

                    // Check if the column is a primary key
                    ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, null, tableName);
                    while (primaryKeyResultSet.next()) {
                        String primaryKeyColumn = primaryKeyResultSet.getString("COLUMN_NAME");
                        if (primaryKeyColumn.equals(columnName)) {
                            columnInfo += ", PK";
                            break;
                        }
                    }

                    // Check if the column is auto-increment
                    ResultSet autoIncrementResultSet = metaData.getColumns(null, null, tableName, columnName);
                    while (autoIncrementResultSet.next()) {
                        boolean isAutoIncrement = autoIncrementResultSet.getBoolean("IS_AUTOINCREMENT");
                        if (isAutoIncrement) {
                            columnInfo += ", AI";
                            break;
                        }
                    }

                    // Check if the column is part of a foreign key
                    ResultSet foreignKeysResultSet = metaData.getImportedKeys(null, null, tableName);
                    while (foreignKeysResultSet.next()) {
                        String foreignKeyName = foreignKeysResultSet.getString("FKCOLUMN_NAME");
                        String primaryKeyTable = foreignKeysResultSet.getString("PKTABLE_NAME");
                        String primaryKeyColumn = foreignKeysResultSet.getString("PKCOLUMN_NAME");

                        if (foreignKeyName.equals(columnName)) {
                            columnInfo += ", References " + primaryKeyTable + "." + primaryKeyColumn;
                            break;
                        }
                    }

                    columnInfo += ")";
                    System.out.println("  " + columnInfo);
                }

                System.out.println();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
