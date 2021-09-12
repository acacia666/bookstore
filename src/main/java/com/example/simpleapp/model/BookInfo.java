package com.example.simpleapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.print.Book;
import java.util.List;
@Getter
@Setter
public class BookInfo {

    private String author_name;
    private String book_name;
    private int id;
    private float price;
    private Boolean is_recommended;

    public BookInfo(String author_name, String book_name, int id, float price) {
        this.author_name = author_name;
        this.book_name = book_name;
        this.id = id;
        this.price = price;
    }
    public BookInfo(){

    }


}
