package com.example.ethan.studying;

public class Rating {
    private String raterID,
            month, day, year;

    private Boolean showName = true;

    public Rating () {
    }

    public Rating(String raterID, String month, String day, String year, Boolean showName) {
        this.raterID = raterID;
        this.month = month;
        this.day = day;
        this.year = year;
        this.showName = showName;
    }

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
