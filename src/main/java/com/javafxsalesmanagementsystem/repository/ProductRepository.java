package com.javafxsalesmanagementsystem.repository;

import com.javafxsalesmanagementsystem.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}
