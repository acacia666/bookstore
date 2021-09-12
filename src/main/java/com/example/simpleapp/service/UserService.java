package com.example.simpleapp.service;

import com.example.simpleapp.exception.HandledException;
import com.example.simpleapp.model.BookInfo;
import com.example.simpleapp.model.UserInfo;
import com.example.simpleapp.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public UserInfo getCurrentUser(String username) throws java.text.ParseException {
       // int x = userInfo.count();
        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "dd-MM-yyyy";

        UserInfo user = userRepository.getUser(username);
        String datePlusOneDay=addOneDay(user.getDate_of_birth().toString());
        Date newDate=new SimpleDateFormat("yyyy-MM-dd").parse(datePlusOneDay);
        user.setDate_of_birth(newDate);

        return user;
    }
    public Float getUsersOrdersPrice(List<Integer> list,String username) throws ParseException, JsonProcessingException {
        List<BookInfo> order = userRepository.orderBook(list,username);
        float sum = 0;
        for(int i =0; i<order.size();i++){
            sum= sum + order.get(i).getPrice();
        }
        return  sum;
    }
    public Boolean deleteUser(String username){
        userRepository.deleteOrder(username);
        return new Boolean(true);
    }

    public void addUser(JSONObject json) throws Exception {
        userRepository.addUser(json);
    }
    public List<BookInfo> getRecommendedBook() throws JsonProcessingException, HandledException {
        return userRepository.getRecommendedBook();
    }
    public String getUserFromToken(String token){

        String usernamepassword = token.substring(6, token.length());
        byte[] decodedBytes = Base64.getDecoder().decode(usernamepassword);
        usernamepassword = new String(decodedBytes);
        String[] usernameandpassword = usernamepassword.split(":");
        String username = usernameandpassword[0];
        return username;
    }
    static public String addOneDay(String date) {
        return LocalDate.parse(date).plusDays(1).toString();
    }
}
