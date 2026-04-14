package com.ecommercestore.store.mappers;

import com.ecommercestore.store.dtos.RegisterUserRequest;
import com.ecommercestore.store.dtos.UpdateUserRequest;
import com.ecommercestore.store.dtos.UserDto;
import com.ecommercestore.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
