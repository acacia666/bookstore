package com.example.simpleapp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class Order {
    private int user_id;
    private int book_id;
    private float price;

    public Order(int user_id, int book_id, float price) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.price = price;
    }
    public Order() {

    }
}
