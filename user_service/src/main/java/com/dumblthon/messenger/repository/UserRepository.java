package com.dumblthon.messenger.repository;

import com.dumblthon.messenger.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}

