package com.javafxsalesmanagementsystem.controller;

import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    public CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/{customer_id}")
    public Optional<Customer> getCustomer(@PathVariable Long customer_id) {
        return customerService.findCustomerById(customer_id);
    }

    @PostMapping
    public void addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
    }

    @PutMapping("/{customer_id}")
    public void updateCustomer(@RequestBody Customer customer, @PathVariable Long customer_id) {
        customer.setId(customer_id);
        customerService.addCustomer(customer);
    }

    @DeleteMapping("/{customer_id}")
    public void deleteCustomer(@PathVariable Long customer_id) {
        customerService.deleteCustomerById(customer_id);
    }
}
