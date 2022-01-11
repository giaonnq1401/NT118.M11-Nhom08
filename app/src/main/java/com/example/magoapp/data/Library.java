package com.example.magoapp.data;

public class Library {
    String idStory, reader;

    public Library(String idStory, String user) {
        this.idStory = idStory;
        this.reader = user;
    }

    public String getIdStory() {
        return idStory;
    }

    public void setIdStory(String idStory) {
        this.idStory = idStory;
    }

    public String getUser() {
        return reader;
    }

    public void setUser(String user) {
        this.reader = user;
    }
}
