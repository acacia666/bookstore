package com.example.simpleapp.controller;

import com.example.simpleapp.exception.HandledException;
import com.example.simpleapp.model.BookInfo;
import com.example.simpleapp.model.Price;
import com.example.simpleapp.model.UserInfo;
import com.example.simpleapp.repository.UserRepository;
import com.example.simpleapp.service.UserService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Base64;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "login")// no need authen
    public HttpStatus login(@RequestHeader(name = "Authorization") String token) {
        boolean auth = userRepository.checkAuth(token);
        if (auth == false) {
            return HttpStatus.UNAUTHORIZED;
        } else {
            userRepository.addLoggedInUser(token);
        }
        return HttpStatus.OK;
    }

    @GetMapping(path = "users")//need authen
    public ResponseEntity<UserInfo> getUser(@RequestHeader(name = "Authorization") String token)
            throws java.text.ParseException {
        String username = userService.getUserFromToken(token);
        boolean auth = userRepository.checkLoggedInUser(username);
        if(auth==true) {
            return new ResponseEntity<UserInfo>(userService.getCurrentUser(username), HttpStatus.OK);
        }
        else {
            return null;
        }
    }

    @DeleteMapping(path = "users")//need authen
    public ResponseEntity<Boolean> deleteUserOrder(@RequestHeader(name = "Authorization") String token) {
        String username = userService.getUserFromToken(token);
        boolean auth = userRepository.checkLoggedInUser(username);
        if (auth == false) {
            return null;
        } else {
            return new ResponseEntity<Boolean>(userService.deleteUser(username), HttpStatus.OK);
        }
    }

    @PostMapping(path = "users/orders")//need authen
    public ResponseEntity<Price> postUserOrder(@RequestBody Map<String, List<Integer>> payload, @RequestHeader(name = "Authorization") String token) throws ParseException, JsonProcessingException {
        String username = userService.getUserFromToken(token);
        boolean auth = userRepository.checkLoggedInUser(username);
        if (auth == false) {
            return null;
        } else {
            return new ResponseEntity<Price>(new Price(userService.getUsersOrdersPrice(payload.get("orders"), username)), HttpStatus.OK);
        }

    }
    @PostMapping(path = "users")//dont need authen
    public ResponseEntity<Integer> postUser(@RequestBody JSONObject json) throws Exception {
        return new ResponseEntity<Integer>(userService.addUser(json), HttpStatus.OK);
    }

    @GetMapping(path = "books")//no need authen
    public ResponseEntity<List<BookInfo>> getBooks() throws JsonProcessingException, HandledException {
        return new ResponseEntity<List<BookInfo>>(userService.getRecommendedBook(), HttpStatus.OK);
    }



    @PostMapping(path = "test")
    public HttpStatus test(@RequestHeader(name = "Authorization") String token) {
        boolean auth = userRepository.checkAuth(token);
        if (auth == false) {
            return HttpStatus.UNAUTHORIZED;
        } else {

        }
        return HttpStatus.OK;
    }







}
