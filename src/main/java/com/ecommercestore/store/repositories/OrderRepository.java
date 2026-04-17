package com.ecommercestore.store.repositories;

import com.ecommercestore.store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  }