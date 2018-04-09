package com.example.user.mana_livechatv2.model;

/**
 * Created by user on 15/07/2016.
 */
import java.io.Serializable;

public class User implements Serializable {
    String id, name, email, fullname, isNarasumber;

    public User() {
    }

    public User(String id, String name, String email, String fullname, String isNarasumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.fullname = fullname;
        this.isNarasumber = isNarasumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public String isNarasumber() { return isNarasumber; }

}