package com.ecommercestore.store.repositories;

import com.ecommercestore.store.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}