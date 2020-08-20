package com.dumblthon.messenger.auth.repository;

import com.dumblthon.messenger.auth.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long userId);

    Optional<User> findByPhoneNumber(String phoneNumber);

    User save(User user);

}
