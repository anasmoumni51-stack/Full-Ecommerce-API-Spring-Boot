package com.ecommercestore.store.controllers;

import com.ecommercestore.store.dtos.CheckoutRequest;
import com.ecommercestore.store.dtos.CheckoutResponse;
import com.ecommercestore.store.dtos.ErrorDto;
import com.ecommercestore.store.exceptions.CartEmptyException;
import com.ecommercestore.store.exceptions.CartNotFoundException;
import com.ecommercestore.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
