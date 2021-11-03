package com.example.demo.converter;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductRequest;

public class ProductConverter {
    ProductConverter(){};

    public static Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }
}
