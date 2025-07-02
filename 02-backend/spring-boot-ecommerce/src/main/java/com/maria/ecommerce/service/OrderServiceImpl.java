package com.maria.ecommerce.service;

import com.maria.ecommerce.dao.CustomerRepository;
import com.maria.ecommerce.dao.OrderRepository;
import com.maria.ecommerce.dao.ProductRepository;
import com.maria.ecommerce.dto.OrderDTO;
import com.maria.ecommerce.dto.OrderDetailsResponseDTO;
import com.maria.ecommerce.dto.OrderItemDetailsDTO;
import com.maria.ecommerce.entity.Customer;
import com.maria.ecommerce.entity.Order;
import com.maria.ecommerce.entity.OrderItem;
import com.maria.ecommerce.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public List<OrderDTO> getOrderHistory(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        List<Order> orders = orderRepository.findByCustomer(Optional.ofNullable(customer));

        return orders.stream().map(order -> new OrderDTO(
                order.getId(),
                order.getOrderTrackingNumber(),
                order.getDateCreated(),
                order.getTotalPrice(),
                order.getTotalQuantity(),
                order.getStatus() != null ? order.getStatus() : "In elaborazione"
        )).collect(Collectors.toList());
    }

    @Override
    public OrderDetailsResponseDTO getOrderDetails(Long orderId, String email) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);

        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Utente non trovato.");
        }

        Customer customer = customerOpt.get();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato."));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Accesso non autorizzato all'ordine.");
        }

        List<OrderItemDetailsDTO> items = order.getOrderItems().stream().map(item -> new OrderItemDetailsDTO(
                item.getProductId(),
                item.getImageUrl(),
                item.getUnitPrice(),
                item.getQuantity()
        )).toList();

        return new OrderDetailsResponseDTO(orderId, order.getOrderTrackingNumber(), order.getDateCreated(), items);
    }
}
