package com.example.simpleapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Base64;
import java.util.Date;
@Getter
@Setter
public class User {
    private int id;
    private String username;
    private String password;
    private Date date_of_birth;
    private String name;
    private String surname;


    public User(int id, String username, String password, Date date_of_birth, String name, String surname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.date_of_birth = date_of_birth;
        this.name = name;
        this.surname = surname;
    }

    public User() {
    }
    public String getDecodedPassword(){
        byte[] decodedBytes = Base64.getDecoder().decode(this.password);
        return new String(decodedBytes);
    }
}
