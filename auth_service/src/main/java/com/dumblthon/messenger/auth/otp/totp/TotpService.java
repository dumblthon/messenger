package com.dumblthon.messenger.auth.otp.totp;

import org.springframework.stereotype.Service;

@Service
public interface TotpService {

    UserSecret generateSecret(long userId) throws UserAlreadyExistsException;

    boolean validatePassword(long userId, String code);

}
