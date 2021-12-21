package com.example.magoapp.data;

public class Users {
    private String nameUser, emailUser, DoBUser;

    public Users(){}

    public Users(String nameUser, String emailUser, String doBUser) {
        this.nameUser = nameUser;
        this.emailUser = emailUser;
        this.DoBUser = doBUser;
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
}
