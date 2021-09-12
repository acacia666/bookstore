package com.example.simpleapp.rowmapper;

import com.example.simpleapp.model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderInfoRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {

        Order orderInfo = new Order();
        orderInfo.setUser_id(rs.getInt("user_id"));
        orderInfo.setBook_id(rs.getInt("book_id"));
        orderInfo.setPrice(rs.getFloat("price"));


        return orderInfo;

    }
}
