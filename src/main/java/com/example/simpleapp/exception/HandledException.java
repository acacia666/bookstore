package com.example.simpleapp.exception;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

public class HandledException extends Exception {

    public HandledException(String s) {
        super(s);
    }
}