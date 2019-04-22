package com.example.ethan.studying;


import java.util.HashMap;
import java.util.Map;

public class Groups{
    private String groupName;
    private String groupID;
    private Map<String, String> members;

    public Groups(){
    }

    public Groups(String groupName, String groupID, String user) {
        this.groupName=groupName;
        this.groupID=groupID;
        Map<String,Object> members = new HashMap<String,Object>();
        members.put(user, "Group Leader");
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

    public Map<String, String> getMembers() {
        return members;
    }

    public void setMembers(Map<String, String> members) {
        this.members = members;
    }

    //add members
    public void addMembers(String user) {
        members.put(user, "Group Member");
    }

    //Update member type
    public void setMemberType(String user, String userType) {
        if(members.containsKey(user)){
            members.put(user,userType);
        } else {
            System.out.println("User does not exist");
        }
    }

    public void assignLeader(String oldLeader, String newLeader) {
        members.put(oldLeader, "Group Member");
        members.put(newLeader, "Group Leader");
    }
}