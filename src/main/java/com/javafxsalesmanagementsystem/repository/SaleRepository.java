package com.javafxsalesmanagementsystem.repository;

import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.entity.Sale;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends CrudRepository<Sale, Long> {

    List<Sale> findAllSalesByCustomerName_Id(Long customerNameId);

}
