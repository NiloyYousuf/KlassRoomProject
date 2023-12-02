package com.example.Database;

import com.example.klassroom.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AssignmentNotificationTrigger {

    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();

            // Drop the trigger if it already exists
            String dropTriggerSQL = "DROP TRIGGER IF EXISTS assignment_notification_trigger";
            statement.executeUpdate(dropTriggerSQL);

            // Create the trigger
            String createTriggerSQL =
                    "CREATE TRIGGER assignment_notification_trigger AFTER INSERT ON assignments\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "    DECLARE student_username_val VARCHAR(50);\n" +
                            "    DECLARE classroom_code_val VARCHAR(10);\n" +
                            "\n" +
                            "    -- Declare a cursor to select all students in the classroom\n" +
                            "    DECLARE cursor1 CURSOR FOR\n" +
                            "        SELECT csj.student_username, a.classroom_code\n" +
                            "        FROM classroom_student_junction csj\n" +
                            "        JOIN assignments a ON a.classroom_code = csj.classroom_code\n" +
                            "        WHERE a.Assignment_ID = NEW.Assignment_ID;\n" +
                            "\n" +
                            "    -- Declare handlers\n" +
                            "    DECLARE CONTINUE HANDLER FOR NOT FOUND SET student_username_val = NULL;\n" +
                            "\n" +
                            "    -- Open the cursor\n" +
                            "    OPEN cursor1;\n" +
                            "\n" +
                            "    -- Loop through all students\n" +
                            "    read_students: LOOP\n" +
                            "        -- Fetch the next row\n" +
                            "        FETCH cursor1 INTO student_username_val, classroom_code_val;\n" +
                            "        IF student_username_val IS NULL THEN\n" +
                            "            LEAVE read_students;\n" +
                            "        END IF;\n" +
                            "\n" +
                            "        -- Insert notification for each student\n" +
                            "        INSERT INTO assignment_notification (classroom_code, student_username, notification_text)\n" +
                            "        VALUES (classroom_code_val, student_username_val, CONCAT('New assignment uploaded for classroom: ', classroom_code_val));\n" +
                            "    END LOOP;\n" +
                            "\n" +
                            "    -- Close the cursor\n" +
                            "    CLOSE cursor1;\n" +
                            "END;";

            // Execute the trigger creation SQL
            statement.executeUpdate(createTriggerSQL);

      System.out.println("Trigger Created Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
