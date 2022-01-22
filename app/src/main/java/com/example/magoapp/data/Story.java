package com.example.magoapp.data;

import kotlinx.coroutines.Job;

public class Story {
    private String sName, sAuthor, sImage, sDesc, sID;

    public Story(){}

    public Story(String sName, String sAuthor, String sDesc, String sImage) {
        this.sName = sName;
        this.sAuthor = sAuthor;
        this.sImage = sImage;
        this.sDesc = sDesc;
    }

    public Story(String sID) {
        this.sID = sID;
    }

    public Story(String sName, String sDesc, String sImage) {
        this.sName = sName;
        this.sImage = sImage;
        this.sDesc = sDesc;
    }

    public Story(String sName, String sDesc) {
        this.sName = sName;
        this.sDesc = sDesc;
    }


    public String getsID() {
        return sID;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsAuthor() {
        return sAuthor;
    }

    public void setsAuthor(String sAuthor) {
        this.sAuthor = sAuthor;
    }

    public String getsDesc() {
        return sDesc;
    }

    public void setsDesc(String sDesc) {
        this.sDesc = sDesc;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }
}
