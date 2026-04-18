package com.ecommercestore.store.services;

import com.ecommercestore.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
