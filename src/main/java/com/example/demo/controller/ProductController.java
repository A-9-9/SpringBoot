package com.example.demo.controller;

import com.example.demo.entity.product.ProductRequest;
import com.example.demo.entity.product.ProductResponse;
import com.example.demo.parameter.ProductParameter;
import com.example.demo.service.MailService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private MailService mailService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") String id) {
        ProductResponse productResponse = productService.getProductResponse(id);
        productService.getProductResponse(id);
        productService.getProductResponse(id);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.createProduct(request);
        mailService.sendNewProductMail(productResponse.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productResponse.getId())
                .toUri();
        return ResponseEntity.created(location).body(productResponse);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> modifyProduct(@PathVariable("id") String id, @Valid @RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.replaceProduct(id, request);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(id);
        mailService.sendDeleteProductMail(id);
        return ResponseEntity.noContent().build();

//        ResponseEntity.notFound().build();

//        If the entity not found, ProductService will throw the exception,
//        so under the Controller, doesn't need to check the exception.
    }



    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProductsWithMultipleParam(@ModelAttribute ProductParameter request) {
        List<ProductResponse> productResponses = productService.find(request);
        return ResponseEntity.ok(productResponses);
    }
}
