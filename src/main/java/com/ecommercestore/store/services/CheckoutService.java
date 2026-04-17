package com.ecommercestore.store.services;

import com.ecommercestore.store.dtos.CheckoutRequest;
import com.ecommercestore.store.dtos.CheckoutResponse;
import com.ecommercestore.store.entities.Order;
import com.ecommercestore.store.exceptions.CartEmptyException;
import com.ecommercestore.store.exceptions.CartNotFoundException;
import com.ecommercestore.store.repositories.CartRepository;
import com.ecommercestore.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }
}
