package com.javafxsalesmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    @Column(name = "date",  nullable = false)
    private LocalDateTime date;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @OneToMany(mappedBy = "sale", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SaleItem> saleItems;
}
