package com.javafxsalesmanagementsystem.controller;

import com.javafxsalesmanagementsystem.entity.Sale;
import com.javafxsalesmanagementsystem.service.CustomerService;
import com.javafxsalesmanagementsystem.service.SaleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

    public SaleService saleService;
    public CustomerService customerService;

    public SaleController(SaleService saleService, CustomerService customerService) {
        this.saleService = saleService;
        this.customerService = customerService;
    }

    @GetMapping("/{sale_id}")
    public Sale getSale(@PathVariable Long sale_id) {
        return saleService.findSaleById(sale_id);
    }

    @GetMapping("/sales/{customer_id}")
    public List<Sale> getSaleByCustomerId(@PathVariable Long customer_id) {
        List<Sale> sales = saleService.saleRepository.findAllSalesByCustomerName_Id(customer_id);
        return sales;
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
