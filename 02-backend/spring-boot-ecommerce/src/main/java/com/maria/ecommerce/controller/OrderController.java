package com.maria.ecommerce.controller;

import com.maria.ecommerce.dao.CustomerRepository;
import com.maria.ecommerce.dao.OrderRepository;
import com.maria.ecommerce.entity.Customer;
import com.maria.ecommerce.entity.Order;
import com.maria.ecommerce.security.JwtUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("http://localhost:4200")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final JwtUtils jwtUtils;

    public OrderController(OrderRepository orderRepository,
                           CustomerRepository customerRepository,
                           JwtUtils jwtUtils) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/my-orders")
    public List<Order> getOrdersForUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtils.getEmailFromToken(token);

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return orderRepository.findByCustomer(customer);
    }
}
