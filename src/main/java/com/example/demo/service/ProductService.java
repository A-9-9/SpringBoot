package com.example.demo.service;

import com.example.demo.auth.UserIdentity;
import com.example.demo.converter.ProductConverter;
import com.example.demo.entity.product.Product;
import com.example.demo.entity.product.ProductRequest;
import com.example.demo.entity.product.ProductResponse;
import com.example.demo.exception.NotFoundException;
import com.example.demo.parameter.ProductParameter;
import com.example.demo.repository.ProductRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public class ProductService {
    private UserIdentity userIdentity;
    private ProductRepository productRepository;
    private MailService mailService;

    public ProductService(ProductRepository productRepository, MailService mailService, UserIdentity userIdentity) {
        this.productRepository = productRepository;
        this.mailService = mailService;
        this.userIdentity = userIdentity;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = ProductConverter.toProduct(request);
        product.setCreator(userIdentity.getId());
        product = productRepository.insert(product);
        mailService.sendNewProductMail(product.getId());
        return ProductConverter.toProductResponse(product);
    }

    public ProductResponse getProductResponse(String id) {
        Product product = productRepository.
                findById(id).
                orElseThrow(() -> new NotFoundException("Can't find product."));
        return ProductConverter.toProductResponse(product);
    }

    public Product getProduct(String id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public ProductResponse replaceProduct(String id, ProductRequest request) {
        Product oldProduct = getProduct(id);

        Product product = ProductConverter.toProduct(request);
        product.setId(oldProduct.getId());
        product.setCreator(oldProduct.getCreator());
        return ProductConverter.toProductResponse(productRepository.save(product));
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
        mailService.sendDeleteProductMail(id);
    }

    public List<ProductResponse> find(ProductParameter parameter) {
        String keyword = Optional.ofNullable(parameter.getKeyWord()).orElse("");
        int priceFrom = Optional.ofNullable(parameter.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(parameter.getPriceTo()).orElse(Integer.MAX_VALUE);

        Sort sort = getSortingStrategy(parameter.getOrderBy(), parameter.getSortRule());

        List<Product> productList = productRepository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, keyword, sort);

        return productList.stream().map(o -> ProductConverter.toProductResponse(o)).collect(Collectors.toList());
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
