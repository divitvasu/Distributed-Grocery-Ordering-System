package com.GroceryOrderingSystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GroceryOrderingSystemServerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GroceryOrderingSystemServerApplication.class, args);
	}

	public void run(String... args) throws Exception {
//		jdbcTemplate.update("INSERT INTO products (name, quantity) VALUES (?, ?);", "Pepsi", 100);
//		jdbcTemplate.update("INSERT INTO products (name, quantity) VALUES (?, ?);", "Doritos", 100);
//		jdbcTemplate.update("INSERT INTO products (name, quantity) VALUES (?, ?);", "Coke", 200);
//		jdbcTemplate.update("INSERT INTO products (name, quantity) VALUES (?, ?);", "Lays", 500);

//		List<Product> prodcts = jdbcTemplate.query("SELECT * FROM products", new BeanPropertyRowMapper(Product.class));
//
//		for (Product product: prodcts) {
//			System.out.println(product);
//		}
	}
}
