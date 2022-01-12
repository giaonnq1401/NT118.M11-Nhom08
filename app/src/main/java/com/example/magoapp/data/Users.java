package com.example.magoapp.data;

public class Users {
    private String nameUser, emailUser, DoBUser, mImageUrl;
    private String zodiac, hobbies, quotes;

    public Users(){}

    public Users(String nameUser, String emailUser, String doBUser) {
        this.nameUser = nameUser;
        this.emailUser = emailUser;
        this.DoBUser = doBUser;
    }

    public Users(String nameUser, String emailUser, String doBUser, String mImageUrl, String zodiac, String hobbies, String quotes) {
        this.nameUser = nameUser;
        this.emailUser = emailUser;
        DoBUser = doBUser;
        this.mImageUrl = mImageUrl;
        this.zodiac = zodiac;
        this.hobbies = hobbies;
        this.quotes = quotes;
    }

    public Users(String nameUser, String emailUser, String doBUser, String mImageUrl) {
        this.nameUser = nameUser;
        this.emailUser = emailUser;
        DoBUser = doBUser;
        this.mImageUrl = mImageUrl;
    }

    public Users(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getDoBUser() {
        return DoBUser;
    }

    public void setDoBUser(String doBUser) {
        DoBUser = doBUser;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }
}
