package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByNameLikeIgnoreCase(String name, Sort sort);

    List<Product> findByIdIn(List<String> ids);

    List<Product> findByPriceBetweenAndNameLikeIgnoreCase(int priceFrom, int priceTo, String keyword, Sort sort);
}
