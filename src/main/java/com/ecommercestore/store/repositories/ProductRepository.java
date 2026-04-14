package com.ecommercestore.store.repositories;

import com.ecommercestore.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}