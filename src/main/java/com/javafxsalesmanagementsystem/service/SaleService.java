package com.javafxsalesmanagementsystem.service;

import com.javafxsalesmanagementsystem.entity.Sale;
import com.javafxsalesmanagementsystem.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleService {


    public SaleRepository saleRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public void addSales (Sale sale){
        saleRepository.save(sale);
    }

    public void removeSales (Sale sale){
        saleRepository.delete(sale);
    }

}
