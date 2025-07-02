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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public CheckoutServiceImpl(CustomerRepository customerRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        // Retrieve the order info from DTO
        Order order = purchase.getOrder();

        // Generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // Retrieve and attach order items
        Set<OrderItem> orderItems = purchase.getOrderItems();
        for (OrderItem item : orderItems) {

            // Verifica disponibilità prodotto
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato con ID: " + item.getProductId()));

            if (product.getUnitsInStock() < item.getQuantity()) {
                throw new RuntimeException("Prodotto esaurito: " + product.getName());
            }

            // Decrementa lo stock
            product.setUnitsInStock(product.getUnitsInStock() - item.getQuantity());

            // Aggiungi l’item all’ordine
            order.add(item);
        }

        // Imposta indirizzi
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // Gestione Customer
        Customer customer = purchase.getCustomer();

        // Se esiste già, riutilizza lo stesso oggetto dal DB
        Optional<Customer> existingCustomer = customerRepository.findByEmail(customer.getEmail());
        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
        }

        // Associa ordine al cliente
        customer.add(order);

        // Salva il tutto in cascata
        customerRepository.save(customer);

        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {

        // generate a random UUID number (UUID version-4), UNIVERSAL UNIQUE IDENTIFIER
        //creo un unique id, random
        //l'UUID è un metodo per generare ID unici; la probabilità di collisione è molto bassa
        return UUID.randomUUID().toString();
    }
}









