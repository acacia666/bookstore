package com.example.simpleapp.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Price {
    float price;

    public Price(float price) {
        this.price = price;
    }
}
