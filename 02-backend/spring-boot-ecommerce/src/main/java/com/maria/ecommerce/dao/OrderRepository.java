package com.maria.ecommerce.dao;


import com.maria.ecommerce.entity.Customer;
import com.maria.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
}

