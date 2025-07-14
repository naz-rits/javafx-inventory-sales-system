package com.javafxsalesmanagementsystem.controller;


import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    public ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{customer_id}")
    public Optional<Product> getProductById(@PathVariable Long customer_id){
        return productService.findById(customer_id);
    }

    @PostMapping
    public void addProduct(@RequestBody Product product){
        productService.addProduct(product);
    }

    @PutMapping("/{product_id}")
    public void updateProduct(@RequestBody Product product, @PathVariable Long product_id){
        product.setProductId(product_id);
        productService.addProduct(product);
    }

    @DeleteMapping("/{product_id}")
    public void deleteProduct(@PathVariable Long product_id){
        productService.removeProductById(product_id);
    }
}
