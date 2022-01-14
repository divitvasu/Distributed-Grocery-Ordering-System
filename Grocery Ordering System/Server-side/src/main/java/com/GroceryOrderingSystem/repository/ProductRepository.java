package com.GroceryOrderingSystem.repository;

import com.GroceryOrderingSystem.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {
}
