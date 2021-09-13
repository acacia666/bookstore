package com.example.simpleapp.repository;
import com.example.simpleapp.exception.HandledException;
import com.example.simpleapp.model.*;
import com.example.simpleapp.rowmapper.OrderInfoRowMapper;
import com.example.simpleapp.rowmapper.UserRowMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.util.*;

@Repository
public class UserRepository {
    private static String DATABASE_EXCEPTION = "DATABASE_EXCEPTION";
    private static List<User> loggedInUser = new ArrayList<>();
    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserRepository() {

    }

    public UserInfo getUser(String username){
        int id = -1;
        for(int i =0;i<loggedInUser.size();i++){
            if(loggedInUser.get(i).getUsername().equals(username)){
                id=loggedInUser.get(i).getId();
            }
        }

        //Retrieve a UserInfo from database
        String sql ="SELECT * FROM jet_schema.user where id = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());

        //Retrieve List of OrderInfo from database
         sql ="SELECT * FROM jet_schema.order where user_id=?";
        List<Order> order = jdbcTemplate.query(sql, new Object[]{id}, new OrderInfoRowMapper());

        UserInfo result = new UserInfo();
        result.setName(user.getName());
        result.setSurname(user.getSurname());
        result.setDate_of_birth(user.getDate_of_birth());

        result.setBooks(new ArrayList<Integer>());
        for(int i = 0;i<order.size();i++){
            result.getBooks().add(order.get(i).getBook_id());
        }
        return  result;
    }
    public List<Integer> getOrder(){
        return new ArrayList();
    }
    public List<Order> getOrderInfoList(){
        //Retrieve OrderInfo from database
        int id = 2;
        String sql ="SELECT * FROM jet_schema.order where user_id=?";
        List<Order> order = jdbcTemplate.query(sql, new Object[]{id}, new OrderInfoRowMapper());
        return order;
    }
    public List<BookInfo> orderBook(List<Integer> order,String username) throws ParseException, JsonProcessingException {
        //get id from comparing username with loggedInUser
        int id=-1;
        for(int i = 0;i<loggedInUser.size();i++){
            if(loggedInUser.get(i).getUsername().equals(username)){
                id=loggedInUser.get(i).getId();
            }
        }
        //System.out.println("id = "+Integer.toString(id));

        //retrieve string of json list from external api
        String uri = "https://scb-test-book-publisher.herokuapp.com/books";
        RestTemplate restTemplate = new RestTemplate();
        String book_Json_List_String = restTemplate.getForObject(uri, String.class);

        //convert String to List of json
        ObjectMapper mapper = new ObjectMapper();
        List<BookInfo> bookList;
        bookList = Arrays.asList(mapper.readValue(book_Json_List_String, BookInfo[].class));

        //Find the book in book list by compare the order with
        List<BookInfo> result_Book_List = new ArrayList<>();
        for(int i =0;i<order.size();i++){
            order.get(i);
            for(int j=0;j<bookList.size();j++){
                if(bookList.get(j).getId()==order.get(i)){
                    //add book into list and insert into database
                    result_Book_List.add(bookList.get(j));
                    int user_id = id;
                    int book_id = order.get(i);
                    float price=bookList.get(j).getPrice();
                        String sql ="INSERT INTO jet_schema.order (user_id, book_id, price) " +
                            "VALUES ("+user_id+","+book_id+","+ price+")";

                    jdbcTemplate.update(sql);
                }
            }
        }
        return result_Book_List;
    }

    public boolean deleteOrder(String username){
        int id=-1;
        for(int i =0;i<loggedInUser.size();i++){
            if(loggedInUser.get(i).getUsername().equals(username)){
                id=loggedInUser.get(i).getId();
            }
        }
        String sql2 ="Delete  FROM jet_schema.order where user_id=?";
        for(int i =0;i<loggedInUser.size();i++){
            if(loggedInUser.get(i).getUsername().equals(username)){
                loggedInUser.remove(i);
            }
        }
        return jdbcTemplate.update(sql2, new Object[]{id})==1;
    }

    public void addUser(JSONObject json) throws HandledException, java.text.ParseException {
        //declare variables
        String username = json.get("username").toString();
        List<String> usernameList = Arrays.asList(username.split("\\."));//extract name and surname from username
        String name = usernameList.get(0);
        String surname = usernameList.get(1);
        String password = json.get("password").toString();
        byte[] encodedBytes = Base64.getEncoder().encode(password.getBytes());
        password= new String(encodedBytes);

        String date_of_birth = json.get("date_of_birth").toString();
        String sql;

        //add useraccount to database,auto increase id in database
        String[] datearray= date_of_birth.split("/");
        date_of_birth=datearray[2]+"-"+datearray[1]+"-"+datearray[0];
        try {
            sql = "INSERT INTO jet_schema.user(username, password, date_of_birth,name,surname)  " +
                    "VALUES (\"" + username + "\",\"" + password + "\",\"" + date_of_birth +"\",\""+name +"\",\"" +surname+ "\")";
            jdbcTemplate.update(sql);
        }
        catch (Exception e){
            throw new HandledException("Fail to update user because the username is exists");
        }
    }
    public List<BookInfo> getRecommendedBook()
            throws JsonProcessingException, HandledException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String uri;
        String result;
        List<BookInfo> bookList;
        List<BookInfo> recommendedBookList;
        //get list of book info from external api
             uri = "https://scb-test-book-publisher.herokuapp.com/books";
            result = restTemplate.getForObject(uri, String.class);
            bookList = Arrays.asList(mapper.readValue(result, BookInfo[].class));
        //get list of recommended book from external api
            uri = "https://scb-test-book-publisher.herokuapp.com/books/recommendation";
            result = restTemplate.getForObject(uri, String.class);
            recommendedBookList = Arrays.asList(mapper.readValue(result, BookInfo[].class));
        //Check if the book id from book list is in recommended book
        //if its recommended, set boolean to true
        //else, set it false
        for(int i = 0;i<bookList.size();i++){
            for(int j = 0;j<recommendedBookList.size();j++){
                if(bookList.get(i).getId()==recommendedBookList.get(j).getId()){
                    bookList.get(i).setIs_recommended(new Boolean(true));
                    break;
                }
                else {
                    bookList.get(i).setIs_recommended(new Boolean(false));
                }
            }
        }
        //sort by aphabet
        Collections.sort(bookList, new Comparator<BookInfo>(){
            @Override
            public int compare(BookInfo u1, BookInfo u2) {
                return u1.getBook_name().compareTo(u2.getBook_name());
            }
        });
        //make list of recommended book and non recommended book
        List<BookInfo> result_Recommended= new ArrayList<>();
        List<BookInfo> result_Non_Recommended= new ArrayList<>();
        for(int i =0;i<bookList.size();i++){
            if(bookList.get(i).getIs_recommended().equals(new Boolean(true))){

                result_Recommended.add(bookList.get(i));
            }
            else{
                result_Non_Recommended.add(bookList.get(i));
            }
        }

        //sort result_Recommended by aphabet
        Collections.sort(result_Recommended, new Comparator<BookInfo>(){
            @Override
            public int compare(BookInfo u1, BookInfo u2) {
                return u1.getBook_name().compareTo(u2.getBook_name());
            }
        });

        //sort result_Non_Recommended by aphabet
        Collections.sort(result_Non_Recommended, new Comparator<BookInfo>(){
            @Override
            public int compare(BookInfo u1, BookInfo u2) {
                return u1.getBook_name().compareTo(u2.getBook_name());
            }
        });

        //
        for(int i = 0;i<result_Non_Recommended.size();i++){
            result_Recommended.add(result_Non_Recommended.get(i));
        }
        //Collections.reverse(bookList);
        List<BookInfo> sortedBookList = result_Recommended;
        return sortedBookList;
    }

    public boolean authCheck(String username, String password) {
        boolean authorize= false;
        String sql ="SELECT * FROM jet_schema.user ";
        List<User> user = jdbcTemplate.query(sql, new UserRowMapper());
        for(int i = 0;i<user.size();i++){
            if(user.get(i).getUsername().equals(username) && user.get(i).getPassword().equals(password)){
                authorize=true;
            }
        }
        return authorize;
    }

    public void addLoggedInUser(String token) {
        String usernamepassword= token.substring(6,token.length());
        byte[] decodedBytes = Base64.getDecoder().decode(usernamepassword);
        usernamepassword = new String(decodedBytes);
        String[] usernameandpassword=usernamepassword.split(":");
        String username= usernameandpassword[0];
        String sql ="SELECT * FROM jet_schema.user ";
        List<User> user = jdbcTemplate.query(sql, new UserRowMapper());
        for(int i =0;i<user.size();i++){
            if(username.equals(user.get(i).getUsername())){
                loggedInUser.add(user.get(i));
                break;
            }
        }
    }


    public boolean checkLoggedInUser(String token){
        String usernamepassword = token.substring(6, token.length());
        byte[] decodedBytes = Base64.getDecoder().decode(usernamepassword);
        usernamepassword = new String(decodedBytes);
        String[] usernameandpassword = usernamepassword.split(":");
        String username = usernameandpassword[0];


        String password = usernameandpassword[1];

        boolean logIn=false;
        for(int i = 0;i<loggedInUser.size();i++){
            byte[] decoded = Base64.getDecoder().decode(loggedInUser.get(i).getPassword());
            String decodedpassword = new String(decoded);
            if(loggedInUser.get(i).getUsername().equals(username) && decodedpassword.equals(password)){
                logIn=true;
            }
        }
        return logIn;
    }

    public boolean checkAuth(String token) {
        String usernamepassword = token.substring(6, token.length());
        byte[] decodedBytes = Base64.getDecoder().decode(usernamepassword);
        byte[] encodedBytes = Base64.getEncoder().encode(decodedBytes);

        usernamepassword = new String(decodedBytes);
        String[] usernameandpassword = usernamepassword.split(":");
        String username = usernameandpassword[0];
        String password = usernameandpassword[1];
        encodedBytes = Base64.getEncoder().encode(password.getBytes());
        password = new String(encodedBytes);
        boolean auth = authCheck(username, password);
        return auth;
    }
}
