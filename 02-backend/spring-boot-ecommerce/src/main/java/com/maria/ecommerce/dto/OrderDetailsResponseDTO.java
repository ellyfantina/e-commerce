package com.maria.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDetailsResponseDTO {
    private Long orderId;
    private String orderTrackingNumber;
    private Date date;
    private List<OrderItemDetailsDTO> items;
}
