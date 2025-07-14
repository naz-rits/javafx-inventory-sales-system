package com.javafxsalesmanagementsystem.controller;

import com.javafxsalesmanagementsystem.entity.SaleItem;
import com.javafxsalesmanagementsystem.service.SaleItemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saleitem")
public class SaleItemController {

    public SaleItemService saleItemService;

    public SaleItemController(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

    @GetMapping("/{saleitem_id}")
    public SaleItem getSaleItem(@PathVariable Long saleitem_id){
        return saleItemService.findSaleItemById(saleitem_id);
    }


    @PostMapping
    public void addSaleItem(@RequestBody SaleItem saleItem){
        saleItemService.saveSaleItem(saleItem);
    }


    @PutMapping("/{saleitem_id}")
    public void updateSaleItem(@PathVariable Long saleitem_id, @RequestBody SaleItem saleItem){
        saleItem.setSaleItemId(saleitem_id);
        saleItemService.saveSaleItem(saleItem);
    }

    @DeleteMapping("/{saleitem_id}")
    public void deleteSaleItem(@PathVariable Long saleitem_id){
        saleItemService.removeSaleItemById(saleitem_id);
    }

}
