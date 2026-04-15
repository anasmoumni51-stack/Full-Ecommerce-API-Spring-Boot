package com.ecommercestore.store.mappers;

import com.ecommercestore.store.dtos.CartDto;
import com.ecommercestore.store.dtos.CartItemDto;
import com.ecommercestore.store.entities.Cart;
import com.ecommercestore.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
