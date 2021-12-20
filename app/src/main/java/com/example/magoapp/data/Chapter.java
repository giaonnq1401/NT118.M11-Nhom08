package com.example.magoapp.data;

public class Chapter {
    String nameChapter, contentChapter;

    public Chapter(){}

    public Chapter(String nameChapter, String contentChapter) {
        this.nameChapter = nameChapter;
        this.contentChapter = contentChapter;
    }

    public String getNameChapter() {
        return nameChapter;
    }

    public void setNameChapter(String nameChapter) {
        this.nameChapter = nameChapter;
    }

    public String getContentChapter() {
        return contentChapter;
    }

    public void setContentChapter(String contentChapter) {
        this.contentChapter = contentChapter;
    }
}
