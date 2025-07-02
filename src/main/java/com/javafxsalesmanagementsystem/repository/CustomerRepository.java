package com.javafxsalesmanagementsystem.repository;


import com.javafxsalesmanagementsystem.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByUsernameAndPassword(String username, String password);
}
