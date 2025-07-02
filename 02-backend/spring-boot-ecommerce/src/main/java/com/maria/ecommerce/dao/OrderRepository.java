package com.maria.ecommerce.dao;


import com.maria.ecommerce.entity.Customer;
import com.maria.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Optional<Customer> customer);
}

