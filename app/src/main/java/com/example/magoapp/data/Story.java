package com.example.magoapp.data;

import kotlinx.coroutines.Job;

public class Story {
    private String sName, sAuthor, sTopic, sDesc;
    private Chapter sChapter;

    public Story(){}

    public Story(String sName, String sAuthor, String sDesc, Chapter sChapter) {
        this.sName = sName;
        this.sAuthor = sAuthor;
        this.sDesc = sDesc;
        this.sChapter = sChapter;
    }

    public Chapter getsChapter() {
        return sChapter;
    }

    public void setsChapter(Chapter sChapter) {
        this.sChapter = sChapter;
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
}
