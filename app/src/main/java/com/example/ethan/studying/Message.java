package com.example.ethan.studying;

public class Message {
    String message;
    String messageID;
    String userID;

    public Message(){}

    public Message(String message, String messageID, String userID) {
        this.message = message;
        this.userID = userID;
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

   /** public String toString() {
        return "Message(" + "message-'" + message + '\'' +", user-'" +user +'\'' + ", key-'" +key +'\''+ ')';
    } */
}
