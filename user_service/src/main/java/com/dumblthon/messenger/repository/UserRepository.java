package com.dumblthon.messenger.repository;

import com.dumblthon.messenger.entities.User;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface UserRepository extends CrudRepository<User, Integer> {
    User findById(long id);
}

