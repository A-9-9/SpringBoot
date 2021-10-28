package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.parameter.ProductParameter;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MockProductDAO {

    private final List<Product> productDB = new ArrayList();

    @PostConstruct
    private void init() {
        productDB.add(new Product("P001", "Apple Pie", 20));
        productDB.add(new Product("P002", "Banana Pie", 30));
        productDB.add(new Product("P003", "Guava Pie", 40));
        productDB.add(new Product("P004", "Orange Juice", 25));
        productDB.add(new Product("P005", "Lemon Juice", 15));
    }

    public Product insert(Product product) {
        productDB.add(product);
        return product;
    }


    public Product replace(Product product, String id) {
        Optional<Product> productOptional = find(id);
        productOptional.ifPresent(p -> {
            p.setName(product.getName());
            p.setPrice(product.getPrice());
        });
        return product;
    }

    public void delete(Product product) {
        productDB.removeIf(p -> p.getId().equalsIgnoreCase(product.getId()));
    }

    public Optional<Product> find(String id) {
        return productDB.stream().filter(p -> p.getId().equalsIgnoreCase(id)).findFirst();
    }

    public List<Product> find(ProductParameter param) {
        String keyWord = param.getKeyWord();
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();

        Comparator<Product> productComparator = getProductComparator(orderBy, sortRule);

        return productDB.stream().
                filter(p -> p.getName().toUpperCase().contains(keyWord.toUpperCase())).
                sorted(productComparator).
                collect(Collectors.toList());


    }

    private Comparator<Product> getProductComparator(String orderBy, String sortRule) {
        Comparator<Product> productComparator = (p, p1) -> 0;
        if (Objects.isNull(orderBy) || Objects.isNull(sortRule)) {
            return productComparator;
        }

        if (orderBy.equalsIgnoreCase("price")) {
            productComparator = (p, p1) -> p.getPrice() - p1.getPrice();
        } else if (orderBy.equalsIgnoreCase("name")) {
            productComparator = (p, p1) -> p.getName().toUpperCase().compareTo(p1.getName().toUpperCase());
        }

        return sortRule.equalsIgnoreCase("desc") ? productComparator.reversed() : productComparator;
    }




}
