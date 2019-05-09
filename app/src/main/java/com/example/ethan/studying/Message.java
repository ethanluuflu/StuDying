package com.example.ethan.studying;

/**
 * Message class for the messages in Group Chat
 * Stores information about the message content and who sent the message
 */
public class Message {
    String message;
    String messageID;
    String userID;

    public Message(){}

    //Constructor
    public Message(String message, String messageID, String userID) {
        this.message = message;
        this.userID = userID;
        this.messageID = messageID;
    }

    //Getters and setters
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
