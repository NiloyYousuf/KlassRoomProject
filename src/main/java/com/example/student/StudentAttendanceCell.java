package com.example.student;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class StudentAttendanceCell extends ListCell<StudentAttendance> {

    @Override
    protected void updateItem(StudentAttendance item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Create a label for student name
            Label nameLabel = new Label(item.getStudentUsername());

            // Create a checkbox for marking attendance
            CheckBox checkBox = new CheckBox("Present");
            checkBox.setSelected(item.isPresent());
            checkBox.setOnAction(event -> {
                item.setPresent(checkBox.isSelected());
                // Update the attendance in the database here
            });

            // Create an HBox to hold the label and checkbox
            HBox hbox = new HBox(10, nameLabel, checkBox);

            // Set the custom cell content
            setGraphic(hbox);
        }
    }
}