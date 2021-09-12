package com.example.simpleapp.rowmapper;

import com.example.simpleapp.model.User;
import com.example.simpleapp.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setDate_of_birth(rs.getDate("date_of_birth"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));


        return user;

    }
}