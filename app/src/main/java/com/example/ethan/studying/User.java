package com.example.ethan.studying;

import java.io.Serializable;
import java.util.Map;

//User class for handling users in the application
//Contains the standard email address
//Additionally handles the Rating feature of the application
public class User implements Serializable {
    private String user;
    private String userID;
    private String email;
    private String age;
    private Double avgRating;
    private Map<String, Boolean> groups;

    //Empty constructor
    public User () {
    }

    //Constructor
    public User (String user, String email, String userID) {
        this.user = user;
        this.email = email;
        this.userID = userID;
    }
    //Getters and setters
    public String getUser() {
        return user;
    }
    public void setUser(String user){
        this.user= user;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID){
        this.userID = userID;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email){
        this.email= email;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age){
        this.age= age;
    }
    public Double getRating() {
        return avgRating;
    }
    public void setRating(Double rating){
        this.avgRating = rating;
    }
    public Map<String,Boolean> getGroups() {
        return groups;
    }
    public void setGroups(Map<String,Boolean> groups){
        this.groups=groups;
    }
}