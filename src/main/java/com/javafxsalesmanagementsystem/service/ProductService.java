package com.javafxsalesmanagementsystem.service;

import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void removeProduct(Product product) {
        productRepository.delete(product);
    }

    public void removeProductById(Long product_id) {
        productRepository.deleteById(product_id);
    }
    public List<Product> getAllProduct() {
        Iterable<Product> iterable = productRepository.findAll();
        List<Product> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    public List<Product> findByCategory(String category) {
        Iterable<Product> iterable = productRepository.findByCategory(category);
        List<Product> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
