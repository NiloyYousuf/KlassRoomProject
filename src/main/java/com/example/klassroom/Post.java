package com.example.klassroom;

public class Post {
    private int postId;
    private int classroomId;
    private String postTime;
    private String postDate;
    private String postText;
    private byte[] attachment;

    public Post(int postId, int classroomId, String postTime, String postDate, String postText, byte[] attachment) {
        this.postId = postId;
        this.classroomId = classroomId;
        this.postTime = postTime;
        this.postDate = postDate;
        this.postText = postText;
        this.attachment = attachment;
    }

    // Getters
    public Integer getPostId() {
        return postId;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getPostText() {
        return postText;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    // Setters
    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
}
