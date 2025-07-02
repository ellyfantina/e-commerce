package com.maria.ecommerce.service;

import com.maria.ecommerce.dto.OrderDTO;
import com.maria.ecommerce.dto.OrderDetailsResponseDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getOrderHistory(String email);

    OrderDetailsResponseDTO getOrderDetails(Long orderId, String email);
}
