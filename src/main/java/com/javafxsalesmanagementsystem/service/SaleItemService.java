package com.javafxsalesmanagementsystem.service;

import com.javafxsalesmanagementsystem.entity.Product;
import com.javafxsalesmanagementsystem.entity.SaleItem;
import com.javafxsalesmanagementsystem.repository.SaleItemRepository;
import com.javafxsalesmanagementsystem.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleItemService {


    public SaleItemRepository saleItemRepository;

    @Autowired
    public SaleItemService(SaleItemRepository saleItemRepository) {
        this.saleItemRepository = saleItemRepository;
    }

    public SaleItem findSaleItemById(Long saleitem_id) {
        return saleItemRepository.findById(saleitem_id).orElse(null);
    }

    public void addSales(SaleItem saleItem) {
        saleItemRepository.save(saleItem);
    }

    public boolean canDeleteProduct(Product product) {
        List<SaleItem> activeItems = saleItemRepository.findByProductAndOrderedIsFalse(product);
        return activeItems.isEmpty();
    }

    public void saveSaleItem (SaleItem saleItem){
        saleItemRepository.save(saleItem);
    }

    public void removeSaleItem(SaleItem saleItem){
        saleItemRepository.delete(saleItem);
    }

    public void removeSaleItemById(Long saleitemId) {
        saleItemRepository.deleteById(saleitemId);
    }
}
