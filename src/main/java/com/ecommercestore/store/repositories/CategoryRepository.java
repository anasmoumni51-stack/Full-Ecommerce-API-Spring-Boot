package com.ecommercestore.store.repositories;

import com.ecommercestore.store.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}