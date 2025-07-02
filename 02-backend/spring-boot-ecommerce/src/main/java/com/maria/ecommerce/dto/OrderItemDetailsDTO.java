package com.maria.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemDetailsDTO {
    private Long productId;
    private String imageUrl;
    private BigDecimal unitPrice;
    private int quantity;
}
