package com.example.magoapp.data;

public class Reading {
    String idStory, user;

    public Reading(String idStory, String user) {
        this.idStory = idStory;
        this.user = user;
    }

    public String getIdStory() {
        return idStory;
    }

    public void setIdStory(String idStory) {
        this.idStory = idStory;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
