package com.ecommercestore.store.repositories;

import com.ecommercestore.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}