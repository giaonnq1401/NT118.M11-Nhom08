package com.example.magoapp.data;

public class Topic {
    String idTopic, nameTopic;

    public Topic(){}

    public Topic(String idTopic, String nameTopic) {
        this.idTopic = idTopic;
        this.nameTopic = nameTopic;
    }

    public String getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(String idTopic) {
        this.idTopic = idTopic;
    }

    public String getNameTopic() {
        return nameTopic;
    }

    public void setNameTopic(String nameTopic) {
        this.nameTopic = nameTopic;
    }
}
