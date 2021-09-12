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
import java.util.Base64;
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

        UserInfo temp = userRepository.getUser(username);

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        System.out.println("date = "+temp.getDate_of_birth().toString());
        String[] date=temp.getDate_of_birth().toString().split("-");
        System.out.println("date[] = "+date.toString());
        String dateStringFormatted=date[2]+"-"+date[1]+"-"+date[0];
        System.out.println("date formatted = "+dateStringFormatted);
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(dateStringFormatted);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date inputDate = dateFormat.parse(dateStringFormatted);
        temp.setDate_of_birth(date1);

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        Date startDate;
        startDate = df.parse(dateStringFormatted);
        String newDateString = df.format(startDate);
        System.out.println(newDateString);


        return temp;
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

    public int addUser(JSONObject json) throws Exception {
        userRepository.addUser(json);
        return 1;
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
}
