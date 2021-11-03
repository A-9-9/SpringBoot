package com.example.demo.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class ProductRequest {
    @NotEmpty(message = "Product name is undefined.")
    private String name;
    @Min(value = 0, message = "Price should be equal or grater to 0.")
    private Integer price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
