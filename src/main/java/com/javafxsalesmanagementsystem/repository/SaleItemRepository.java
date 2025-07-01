package com.javafxsalesmanagementsystem.repository;

import com.javafxsalesmanagementsystem.entity.SaleItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SaleItemRepository extends CrudRepository<SaleItem, Long> {
}
