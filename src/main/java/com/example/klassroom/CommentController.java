package com.example.klassroom;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CommentController {
    @FXML
    private Text commentText;

    @FXML
    private Text commentDate;

    @FXML
    private Text commentTime;


    public void setCommentText(String text) {
        commentText.setText(text);
    }
    public void setCommentDateText(String text) {
        commentDate.setText(text);
    }
    public void setCommentTimeText(String text) {
        commentTime.setText(text);
    }


}
