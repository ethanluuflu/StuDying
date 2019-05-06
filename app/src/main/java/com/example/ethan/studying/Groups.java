package com.example.ethan.studying;


import java.util.HashMap;
import java.util.Map;

public class Groups{
    private String groupID;
    private String groupName;
    private String groupSubject;
    private String groupDescription;
    public Groups(){
    }

    public Groups(String groupName) {
        this.groupName = groupName;
        this.groupSubject = "Math";
        this.groupDescription = "This group is great! Recommended";
    }
    public Groups(String groupName, String groupID, String user) {
        this.groupName=groupName;
        this.groupID=groupID;
        this.groupSubject = "Math";
        this.groupDescription = "This group is great! Recommended";
    }

    public Groups(String groupID, String groupName, String groupSubject, String groupDescription) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.groupSubject = groupSubject;
        this.groupDescription = groupDescription;
    }

    //Getters and Setters
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupSubject() {
        return groupSubject;
    }

    public void setGroupSubject(String groupSubject) {
        this.groupSubject = groupSubject;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }
}