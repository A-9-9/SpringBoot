package com.example.demo.converter;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductRequest;
import com.example.demo.entity.ProductResponse;

public class ProductConverter {
    ProductConverter(){};

    public static Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }

    public static ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setPrice(product.getPrice());
        response.setName(product.getName());

        return response;
    }
}
