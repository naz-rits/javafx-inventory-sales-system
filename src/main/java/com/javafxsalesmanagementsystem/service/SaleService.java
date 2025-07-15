package com.javafxsalesmanagementsystem.service;

import com.javafxsalesmanagementsystem.entity.Customer;
import com.javafxsalesmanagementsystem.entity.Sale;
import com.javafxsalesmanagementsystem.repository.CustomerRepository;
import com.javafxsalesmanagementsystem.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {


    public SaleRepository saleRepository;
    public  CustomerRepository customerRepository;

    public SaleService(SaleRepository saleRepository, CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
    }

    public void addSales (Sale sale){
        saleRepository.save(sale);
    }

    public void removeSales (Sale sale){
        saleRepository.delete(sale);
    }

    public Sale findSaleById (Long sale_id){
        return saleRepository.findById(sale_id).orElse(null);
    }

    public void removeSalesById(Long id) {
        saleRepository.deleteById(id);
    }

    public List<Sale> findSalesByCustomer(Long customer_id){
        List<Sale> sales = saleRepository.findAllSalesByCustomerName_Id(customer_id);

        return sales;
    }
}
