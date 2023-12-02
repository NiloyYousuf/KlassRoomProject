package com.example.Database;

import com.example.klassroom.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CommentNotificationTrigger {

    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Drop the trigger if it already exists
            String dropTriggerSQL = "DROP TRIGGER IF EXISTS after_insert_comment";
            statement.executeUpdate(dropTriggerSQL);
            System.out.println("Trigger dropped successfully if it existed.");

            // Create trigger
            String createTriggerSQL =
                    "CREATE TRIGGER after_insert_comment AFTER INSERT ON comments\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "    DECLARE done INT DEFAULT FALSE;\n" +
                            "    DECLARE studentUsername VARCHAR(50);\n" +
                            "    DECLARE classroomName VARCHAR(255);\n" +
                            "\n" +
                            "    -- Declare cursor for selecting student usernames\n" +
                            "    DECLARE studentCursor CURSOR FOR\n" +
                            "        SELECT student_username\n" +
                            "        FROM classroom_student_junction\n" +
                            "        WHERE classroom_code = (SELECT classroom_code FROM classrooms WHERE classroom_id = (SELECT classroom_id FROM posts WHERE post_id = NEW.postID LIMIT 1) LIMIT 1);\n" +
                            "\n" +
                            "    -- Declare continue handler for the cursor\n" +
                            "    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;\n" +
                            "\n" +
                            "    -- Get teacher_username and classroom_name for the given classroom\n" +
                            "    SELECT teacher_username, subject_name INTO @teacherUsername, @classroomName\n" +
                            "    FROM classrooms\n" +
                            "    WHERE classroom_id = (SELECT classroom_id FROM posts WHERE post_id = NEW.postID LIMIT 1);\n" +
                            "\n" +
                            "    -- Insert notification for teacher\n" +
                            "    INSERT INTO comment_notification (classroom_code, post_id, username, notification_text)\n" +
                            "    VALUES ((SELECT classroom_code FROM classrooms WHERE classroom_id = (SELECT classroom_id FROM posts WHERE post_id = NEW.postID LIMIT 1)), NEW.postID, @teacherUsername,\n" +
                            "            CONCAT('New comment in the post of Classroom ', @classroomName));\n" +
                            "\n" +
                            "    -- Open the cursor\n" +
                            "    OPEN studentCursor;\n" +
                            "\n" +
                            "    -- Loop through all students\n" +
                            "    read_students: LOOP\n" +
                            "        -- Fetch the next student\n" +
                            "        FETCH studentCursor INTO studentUsername;\n" +
                            "        IF done THEN\n" +
                            "            LEAVE read_students;\n" +
                            "        END IF;\n" +
                            "\n" +
                            "        -- Insert notification for student\n" +
                            "        INSERT INTO comment_notification (classroom_code, post_id, username, notification_text)\n" +
                            "        VALUES ((SELECT classroom_code FROM classrooms WHERE classroom_id = (SELECT classroom_id FROM posts WHERE post_id = NEW.postID LIMIT 1)), NEW.postID, studentUsername,\n" +
                            "                CONCAT('New comment in the post of Classroom ', @classroomName));\n" +
                            "    END LOOP;\n" +
                            "\n" +
                            "    -- Close the cursor\n" +
                            "    CLOSE studentCursor;\n" +
                            "END;";

            statement.executeUpdate(createTriggerSQL);
            System.out.println("Trigger created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
