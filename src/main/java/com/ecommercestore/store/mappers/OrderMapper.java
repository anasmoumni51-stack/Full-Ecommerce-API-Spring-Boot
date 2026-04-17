package com.ecommercestore.store.mappers;

import com.ecommercestore.store.dtos.OrderDto;
import com.ecommercestore.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
