package com.example.Comment;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CommentTileController {

    @FXML
    private HBox commentTile;

    @FXML
    private Text commentText;

    public void setCommentText(String text) {
        commentText.setText(text);
    }
}
