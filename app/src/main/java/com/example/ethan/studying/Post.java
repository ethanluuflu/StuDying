package com.example.ethan.studying;

import com.google.firebase.database.ServerValue;

import java.util.Calendar;

public class Post {

    private String postID;
    private String title;
    private String content;
    private String userID;
    private String time;

    public Post() {
        //Empty Constructor
    }
    public Post(String postID, String title, String content, String userID) {
        this.postID = postID;
        this.title = title;
        this.content = content;
        this.userID = userID;
        Calendar c = Calendar.getInstance();
        String[] month = {"January","February", "March", "April","May", "June", "July", "August", "September", "October", "November", "December"};
        this.time = month[c.get(Calendar.MONTH)-1] + " " + String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + ", " + String.valueOf(c.get(Calendar.YEAR));
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
