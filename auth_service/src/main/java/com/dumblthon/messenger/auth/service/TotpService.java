package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.exception.UserAlreadyExistsException;
import com.dumblthon.messenger.auth.model.UserSecret;
import org.springframework.stereotype.Service;

@Service
public interface TotpService {

    UserSecret generateSecret(long userId) throws UserAlreadyExistsException;

    boolean validatePassword(long userId, String code);

}
