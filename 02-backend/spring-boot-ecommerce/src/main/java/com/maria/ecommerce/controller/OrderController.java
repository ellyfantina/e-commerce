package com.maria.ecommerce.controller;

import com.maria.ecommerce.dao.CustomerRepository;
import com.maria.ecommerce.dao.OrderRepository;
import com.maria.ecommerce.dto.OrderDTO;
import com.maria.ecommerce.dto.OrderDetailsResponseDTO;
import com.maria.ecommerce.entity.Customer;
import com.maria.ecommerce.entity.Order;
import com.maria.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDTO>> getUserOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Customer> customer = customerRepository.findByEmail(email);

        if (customer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Order> orders = orderRepository.findByCustomer(Optional.of(customer.get()));

        List<OrderDTO> result = orders.stream().map(order ->
                new OrderDTO(
                        order.getId(),
                        order.getOrderTrackingNumber(),
                        order.getDateCreated(),
                        order.getTotalPrice(),
                        order.getTotalQuantity(),
                        order.getStatus() != null ? order.getStatus() : "In elaborazione"
                )
        ).toList();


        return ResponseEntity.ok(result);
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDetailsResponseDTO> getOrderDetails(@PathVariable Long orderId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderDetailsResponseDTO details = orderService.getOrderDetails(orderId, email);
        return ResponseEntity.ok(details);
    }
}
