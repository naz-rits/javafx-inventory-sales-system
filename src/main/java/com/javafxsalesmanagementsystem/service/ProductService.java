package com.javafxsalesmanagementsystem.service;

import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void removeProduct(Product product) {
        productRepository.delete(product);
    }
}
