package com.ecommercestore.store.controllers;

import com.ecommercestore.store.dtos.CheckoutRequest;
import com.ecommercestore.store.dtos.CheckoutResponse;
import com.ecommercestore.store.dtos.ErrorDto;
import com.ecommercestore.store.exceptions.CartEmptyException;
import com.ecommercestore.store.exceptions.CartNotFoundException;
import com.ecommercestore.store.exceptions.PaymentException;
import com.ecommercestore.store.repositories.OrderRepository;
import com.ecommercestore.store.services.CheckoutService;
import com.ecommercestore.store.services.WebhookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handleWebhook(
        @RequestHeader Map<String, String> headers,
        @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }


    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
