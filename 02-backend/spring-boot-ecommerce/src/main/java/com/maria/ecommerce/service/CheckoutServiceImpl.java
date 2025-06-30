package com.maria.ecommerce.service;

import com.maria.ecommerce.dao.CustomerRepository;
import com.maria.ecommerce.dao.ProductRepository;
import com.maria.ecommerce.dto.Purchase;
import com.maria.ecommerce.dto.PurchaseResponse;
import com.maria.ecommerce.entity.Product;
import com.maria.ecommerce.entity.Customer;
import com.maria.ecommerce.entity.Order;
import com.maria.ecommerce.entity.OrderItem;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    public CheckoutServiceImpl(CustomerRepository customerRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato con ID: " + item.getProductId()));

            if (product.getUnitsInStock() < item.getQuantity()) {
                throw new RuntimeException("Prodotto esaurito: " + product.getName());
            }

            product.setUnitsInStock(product.getUnitsInStock() - item.getQuantity());

            order.add(item);
        }

        // populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // populate customer with order
        Customer customer = purchase.getCustomer();
        customer.add(order);

        // save to the database
        customerRepository.save(customer);

        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {

        // generate a random UUID number (UUID version-4), UNIVERSAL UNIQUE IDENTIFIER
        //creo un unique id, random
        //l'UUID è un metodo per generare ID unici; la probabilità di collisione è molto bassa
        return UUID.randomUUID().toString();
    }
}









