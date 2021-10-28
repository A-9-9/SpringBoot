package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.UnprocessableEntityException;
import com.example.demo.parameter.ProductParameter;
import com.example.demo.repository.MockProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private MockProductDAO productDAO;

    public Product createProduct(Product request) {
        boolean isIdDuplicate = productDAO.find(request.getId()).isPresent();
        if (!isIdDuplicate) {
            throw new UnprocessableEntityException("The id of the product is duplicated.");
        }

        Product product = new Product();
        product.setPrice(request.getPrice());
        product.setName(request.getName());
        product.setId(request.getId());

        return productDAO.insert(product);
    }

    public Product getProduct(String id) {
        Optional<Product> productOptional = productDAO.find(id);

        if (!productOptional.isPresent()) {
            throw new NotFoundException("Can't find product.");
        }

        return productOptional.get();

//        return productDAO.find(id).orElseThrow(() -> new NotFoundException(""));
    }

    public Product replaceProduct(String id, Product request) {
        Product product = getProduct(id);
        return productDAO.replace(request, product.getId());
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productDAO.delete(product);
    }

    public List<Product> find(ProductParameter parameter) {
        return productDAO.find(parameter);
    }
}
