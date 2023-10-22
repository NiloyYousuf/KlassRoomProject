package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.Blob;
import java.io.InputStream;

public class CommentController {
    @FXML
    private Text commentText;
    @FXML
    private Text commentDate;
    @FXML
    private Text commentTime;
    @FXML
    private ImageView Uploader_image;
    @FXML
    private Text user_name;

    private int comment_ID;

    public void setCommentText(String text) {
        commentText.setText(text);
    }

    public void setCommentDateText(String text) {
        commentDate.setText(text);
    }

    public void setCommentTimeText(String text) {
        commentTime.setText(text);
    }

    public void setCommentUsername(String text) {
        user_name.setText(text);
    }

    public void setComment_ID(int ID) {
        comment_ID = ID;
    }

    public void setUploaderImage(Blob photoBlob) {
        if (photoBlob != null) {
            try {
                // Load the image from the Blob data
                InputStream inputStream = photoBlob.getBinaryStream();
                Image image = new Image(inputStream);
                Uploader_image.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Handle null Blob (no image available)
            // You can set a default image or leave the image blank
            // For example, setting a blank image:
            Image blankImage = new Image(getClass().getResourceAsStream("user.png"));
            Uploader_image.setImage(blankImage);
        }
    }

}
