package com.example.simpleapp.rowmapper;

import com.example.simpleapp.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoRowMapper implements RowMapper<UserInfo> {

    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

        UserInfo customer = new UserInfo();
        customer.setName(rs.getString("name"));
        customer.setSurname(rs.getString("surname"));
        customer.setDate_of_birth(rs.getDate("date_of_birth"));


        return customer;

    }
}