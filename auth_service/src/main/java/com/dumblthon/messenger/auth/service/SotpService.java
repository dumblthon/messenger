package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.model.UserInfo;

import java.util.Optional;

public interface SotpService {

    Optional<UserInfo> findUser(long userId);

    Optional<UserInfo> findUser(String username);

    UserInfo authenticate(String username);

    boolean validatePassword(long userId, String code);

}
