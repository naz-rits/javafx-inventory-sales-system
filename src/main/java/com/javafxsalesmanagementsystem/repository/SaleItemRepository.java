package com.javafxsalesmanagementsystem.repository;

import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.entity.SaleItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SaleItemRepository extends CrudRepository<SaleItem, Long> {

    List<SaleItem> findByProductAndOrderedIsFalse(Product product);
}
