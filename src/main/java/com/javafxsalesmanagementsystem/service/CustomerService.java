package com.javafxsalesmanagementsystem.service;

import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.entity.Sale;
import com.javafxsalesmanagementsystem.repository.CustomerRepository;
import com.javafxsalesmanagementsystem.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    public CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Optional<Customer> findCustomerByUsernameAndPassword(String username, String password) {
        return customerRepository.findByUsernameAndPassword(username, password);
    }

    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

}
