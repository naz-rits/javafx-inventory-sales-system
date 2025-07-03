package com.javafxsalesmanagementsystem.service;

import com.javafxsalesmanagementsystem.entity.SaleItem;
import com.javafxsalesmanagementsystem.repository.SaleItemRepository;
import com.javafxsalesmanagementsystem.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleItemService {


    public SaleItemRepository saleItemRepository;

    @Autowired
    public SaleItemService(SaleItemRepository saleItemRepository) {
        this.saleItemRepository = saleItemRepository;
    }

    public void saveSaleItem (SaleItem saleItem){
        saleItemRepository.save(saleItem);
    }
}
