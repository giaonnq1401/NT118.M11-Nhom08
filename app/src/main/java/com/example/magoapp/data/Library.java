package com.example.magoapp.data;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Library {
    String idStory, user;

    public Library(String idStory, String user) {
        this.idStory = idStory;
        this.user = user;
    }

    public Library(String idStory) {
        this.idStory = idStory;
    }

//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put(,idStory);
//        return result;
//    }

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
