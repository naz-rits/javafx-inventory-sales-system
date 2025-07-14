package com.javafxsalesmanagementsystem.controller;

import com.javafxsalesmanagementsystem.entity.Sale;
import com.javafxsalesmanagementsystem.service.SaleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    public SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping("/{sale_id}")
    public Sale getSale(@PathVariable Long sale_id) {
        return saleService.findSaleById(sale_id);
    }

    @PostMapping
    public void addSale(@RequestBody Sale sale){
        saleService.addSales(sale);
    }

    @PutMapping("/{sale_id}")
    public void updateSale(@PathVariable Long sale_id, @RequestBody Sale sale){
        sale.setSaleId(sale_id);
        saleService.addSales(sale);
    }

    @DeleteMapping("/{sale_id}")
    public void deleteSale(@PathVariable Long sale_id){
        saleService.removeSalesById(sale_id);
    }
}
