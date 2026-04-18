package com.ecommercestore.store.services;

import com.ecommercestore.store.dtos.CheckoutRequest;
import com.ecommercestore.store.dtos.CheckoutResponse;
import com.ecommercestore.store.entities.Order;
import com.ecommercestore.store.exceptions.CartEmptyException;
import com.ecommercestore.store.exceptions.CartNotFoundException;
import com.ecommercestore.store.repositories.CartRepository;
import com.ecommercestore.store.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    @Value("${websiteUrl}")
    private String websiteUrl;

    public CheckoutResponse checkout(CheckoutRequest request) throws StripeException {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        // Create a checkout session
        var builder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
            .setCancelUrl(websiteUrl + "/checkout-cancel");

        order.getItems().forEach(item -> {
            var lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmountDecimal(item.getUnitPrice())
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(item.getProduct().getName())
                                .build()
                        )
                        .build()
                ).build();
            builder.addLineItem(lineItem);
        });

        var session = Session.create(builder.build());

        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId(), session.getUrl());
    }
}
