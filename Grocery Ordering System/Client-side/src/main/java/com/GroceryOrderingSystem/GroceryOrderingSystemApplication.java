package com.GroceryOrderingSystem;

import com.GroceryOrderingSystem.controller.Authentication;
import com.GroceryOrderingSystem.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class GroceryOrderingSystemApplication implements CommandLineRunner {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private KafkaTemplate<String, CartItem> kafkaTemplate;

	private static final String TOPIC = "channel";
	public static final String RES_TOPIC = "response_userone";

	public static void main(String[] args) {
		SpringApplication.run(GroceryOrderingSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String username = "userone";
		boolean res = Authentication.ClientAuthentication(jdbcTemplate, username, "123456");
		System.out.println("Authentication: " + res);

		if (res) {
			kafkaTemplate.send(TOPIC, new CartItem("Pepsi", 2, username));
			kafkaTemplate.send(TOPIC, new CartItem("Coke", 500, username));
			kafkaTemplate.send(TOPIC, new CartItem("Mountain Dew", 1, username));
			kafkaTemplate.send(TOPIC, new CartItem("Coke", 5, username));
		}
	}

}
