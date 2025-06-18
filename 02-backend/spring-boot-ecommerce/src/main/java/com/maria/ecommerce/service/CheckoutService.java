package com.maria.ecommerce.service;

import com.maria.ecommerce.dto.Purchase;
import com.maria.ecommerce.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}
