package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.model.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TotpService {

    Optional<UserInfo> findUser(long userId);

    Optional<UserInfo> findUser(String username);

    UserInfo createUser(String username);

    boolean validatePassword(long userId, String code);

}
