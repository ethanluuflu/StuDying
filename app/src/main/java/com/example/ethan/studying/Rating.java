package com.example.ethan.studying;

//Rating class to handle the rating operation for groups
//When user rates members of the group, a rating object is created
public class Rating {
    private String raterID,
            month, day, year;

    private Boolean showName = true;

    public Rating () {
    }

    //Constructor
    public Rating(String raterID, String month, String day, String year, Boolean showName) {
        this.raterID = raterID;
        this.month = month;
        this.day = day;
        this.year = year;
        this.showName = showName;
    }

    //getters and setters
    public String getRaterID() {
        return raterID;
    }

    public void setRaterID(String raterID) {
        this.raterID = raterID;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }
}
