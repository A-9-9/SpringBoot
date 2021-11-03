package com.example.demo.service;

import com.example.demo.converter.ProductConverter;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductRequest;
import com.example.demo.exception.NotFoundException;
import com.example.demo.parameter.ProductParameter;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(ProductRequest request) {
        Product product = ProductConverter.toProduct(request);
        return productRepository.insert(product);
    }

    public Product getProduct(String id) {
        return productRepository.
                findById(id).
                orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Product replaceProduct(String id, ProductRequest request) {
        Product oldProduct = getProduct(id);

        Product product = ProductConverter.toProduct(request);
        product.setId(oldProduct.getId());
        
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public List<Product> find(ProductParameter parameter) {
        String keyword = Optional.ofNullable(parameter.getKeyWord()).orElse("");
        int priceFrom = Optional.ofNullable(parameter.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(parameter.getPriceTo()).orElse(Integer.MAX_VALUE);

        Sort sort = getSortingStrategy(parameter.getOrderBy(), parameter.getSortRule());

        return productRepository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, keyword, sort);
    }

    private Sort getSortingStrategy(String orderBy, String sortRule) {
        Sort sort = Sort.unsorted();

        if (!(Objects.isNull(orderBy) || Objects.isNull(sortRule))) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = Sort.by(direction, orderBy);
        }

        return sort;


    }
}
