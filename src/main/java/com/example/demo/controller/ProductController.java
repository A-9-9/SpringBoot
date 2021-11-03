package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductRequest;
import com.example.demo.parameter.ProductParameter;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = productService.createProduct(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(location).body(product);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> modifyProduct(@PathVariable("id") String id, @Valid @RequestBody ProductRequest request) {
        Product product = productService.replaceProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();

//        ResponseEntity.notFound().build();

//        If the entity not found, ProductService will throw the exception,
//        so under the Controller, doesn't need to check the exception.
    }



    @GetMapping
    public ResponseEntity<List<Product>> getProductsWithMultipleParam(@ModelAttribute ProductParameter request) {
        List<Product> products = productService.find(request);
        return ResponseEntity.ok(products);
    }
}
