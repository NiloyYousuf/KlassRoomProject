package com.example.Database;

import com.example.klassroom.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CommentNotificationTrigger {

    public static void main(String[] args) {

        try {Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();

            // Drop the trigger if it already exists
            String dropTriggerSQL = "DROP TRIGGER IF EXISTS after_insert_comment";
            statement.executeUpdate(dropTriggerSQL);
            System.out.println("Trigger dropped successfully if it existed.");

            // Create trigger
            String createTriggerSQL =
                    "CREATE TRIGGER after_insert_comment AFTER INSERT ON comments\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "    DECLARE teacherUsername VARCHAR(50);\n" +
                            "    DECLARE classroomName VARCHAR(255);\n" +
                            "\n" +
                            "    -- Get teacher_username and classroom_name for the given classroom\n" +
                            "    SELECT teacher_username, subject_name INTO teacherUsername, classroomName\n" +
                            "    FROM classrooms\n" +
                            "    WHERE classroom_id = (SELECT classroom_id FROM posts WHERE post_id = NEW.postID LIMIT 1);\n" +
                            "\n" +
                            "    -- Insert notification for teacher if the comment is made by a student\n" +
                            "    IF NEW.student_username IS NOT NULL THEN\n" +
                            "        INSERT INTO comment_notification (classroom_code, post_id, username, notification_text)\n" +
                            "        VALUES ((SELECT classroom_code FROM classrooms WHERE classroom_id = (SELECT classroom_id FROM posts WHERE post_id = NEW.postID LIMIT 1)), NEW.postID, teacherUsername,\n" +
                            "                CONCAT('New comment in the post of Classroom ', classroomName));\n" +
                            "    END IF;\n" +
                            "\n" +
                            "    -- Insert notification for other students in the classroom if the comment is made by a teacher\n" +
                            "    IF NEW.teacher_username IS NOT NULL THEN\n" +
                            "        INSERT INTO comment_notification (classroom_code, post_id, username, notification_text)\n" +
                            "        SELECT DISTINCT csj.classroom_code, NEW.postID, csj.student_username,\n" +
                            "               CONCAT('New comment in the post of Classroom ', classroomName)\n" +
                            "        FROM classroom_student_junction csj\n" +
                            "        WHERE csj.classroom_code = (SELECT classroom_code FROM classrooms WHERE classroom_id = (SELECT classroom_id FROM posts WHERE post_id = NEW.postID LIMIT 1))\n" +
                            "          AND csj.student_username != NEW.student_username; -- Exclude the student who made the comment\n" +
                            "    END IF;\n" +
                            "    -- Insert notification for all other students in the classroom if the comment is made by a student\n" +
                            "    IF NEW.student_username IS NOT NULL THEN\n" +
                            "        INSERT INTO comment_notification (classroom_code, post_id, username, notification_text)\n" +
                            "        SELECT DISTINCT csj.classroom_code, NEW.postID, csj.student_username,\n" +
                            "               CONCAT('New comment in the post of Classroom ', classroomName)\n" +
                            "        FROM classroom_student_junction csj\n" +
                            "        WHERE csj.classroom_code = (SELECT classroom_code FROM classrooms WHERE classroom_id = (SELECT classroom_id FROM posts WHERE post_id = NEW.postID LIMIT 1))\n" +
                            "          AND csj.student_username != NEW.student_username; -- Exclude the student who made the comment\n" +
                            "    END IF;\n" +
                            "END;";

            statement.executeUpdate(createTriggerSQL);
            System.out.println("Trigger created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
