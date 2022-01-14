package com.GroceryOrderingSystem.controller;

import com.GroceryOrderingSystem.model.ClientAuth;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class Authentication {

    private static JdbcTemplate jdbcTemplate;

    public static boolean ClientAuthentication(JdbcTemplate jdbc, String username, String password) {
        jdbcTemplate = jdbc;

//		String sql = "INSERT INTO clients (email, password) VALUES (?, ?)";
//		int res = jdbcTemplate.update(sql, "userfour", "123456");
//
//		if (res > 0) {
//			System.out.println("New row is inserted...");
//		}

        List<ClientAuth> clients = jdbcTemplate.query("SELECT * FROM clients", new BeanPropertyRowMapper(ClientAuth.class));
//        System.out.println(clients);

        for (ClientAuth client: clients) {
            if (client.getEmail().equals(username) && client.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

}
