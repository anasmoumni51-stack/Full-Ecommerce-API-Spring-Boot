package com.ecommercestore.store.mappers;

import com.ecommercestore.store.dtos.UserDto;
import com.ecommercestore.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
