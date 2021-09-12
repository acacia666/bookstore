package com.example.simpleapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserInfo {
    private String name;
    private String surname;
    @JsonFormat(pattern = "dd/MM/yyyy" , shape = JsonFormat.Shape.STRING)
    private Date date_of_birth;
    private List<Integer> books;



    public UserInfo(String name, String surname, Date date_of_birth, List<Integer> books) {
        this.name = name;
        this.surname = surname;
        this.date_of_birth = date_of_birth;
        this.books = books;
    }

    public UserInfo() {

    }
}
