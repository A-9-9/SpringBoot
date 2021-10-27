package com.example.demo.Controller;

import com.example.demo.Entity.Product;
import com.example.demo.parameter.ProductParameter;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private List<Product> productDB;

    @PostConstruct
    public void initDB() {
        productDB = Arrays.asList(
                new Product("P001", "Apple Pie", 20),
                new Product("P002", "Banana Pie", 30),
                new Product("P003", "Guava Pie", 40),
                new Product("P004", "Orange Juice", 25),
                new Product("P005", "Lemon Juice", 15)
        ).stream().collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {

        Optional<Product> productOp = productDB.stream().filter(o -> o.getId().equals(id)).findFirst();

        if (productOp.isPresent()) {
            return ResponseEntity.ok(productOp.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product request) {

        boolean isIdDuplicate = productDB.stream().anyMatch(o -> o.getId().equals(request.getId()));

        if (isIdDuplicate) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        productDB.add(product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(location).body(product);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> modifyProduct(@PathVariable("id") String id, @RequestBody Product request) {
        Optional<Product> productOp = productDB.stream().filter(o -> o.getId().equals(id)).findFirst();

        if (!productOp.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Product product = productOp.get();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") String id) {
        boolean isRemoved = productDB.removeIf(o -> o.getId().equals(id));

        return isRemoved ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }



    @GetMapping
    public ResponseEntity<List<Product>> getProductsWithMultipleParam(@ModelAttribute ProductParameter request) {
        String keyWord = request.getKeyWord();
        String orderBy = request.getOrderBy();
        String sortRule = request.getSortRule();

        Comparator<Product> productComparator = getComparator(orderBy, sortRule);

        List<Product> productList = productDB.stream().
                filter(o -> o.getName().toUpperCase().contains(keyWord.toUpperCase())).
                sorted(productComparator).
                collect(Collectors.toList());

        return ResponseEntity.ok(productList);

    }

    public Comparator<Product> getComparator(String orderBy, String sortRule) {
        Comparator<Product> productComparator = (o, o1) -> 0;

        if (Objects.isNull(orderBy) || Objects.isNull(sortRule)) {
            return productComparator;
        }
        if (orderBy.equalsIgnoreCase("price")) {
            productComparator = (o, o1) -> o.getPrice() - o1.getPrice();
        } else if (orderBy.equalsIgnoreCase("name")) {
            productComparator = (o, o1) -> o.getName().toUpperCase().compareTo(o1.getName().toUpperCase());
        } else {
            productComparator = (o, o1) -> o.getId().toUpperCase().compareTo(o1.getId().toUpperCase());
        }

        return sortRule.equalsIgnoreCase("desc") ?
                productComparator.reversed() :
                productComparator;

    }

}
