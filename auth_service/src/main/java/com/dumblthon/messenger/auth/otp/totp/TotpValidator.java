package com.dumblthon.messenger.auth.otp.totp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class TotpValidator {

    /**
     * Время в секундах в течение которого пароль действителен
     */
    private int windowInSec;

    public TotpValidator(int windowInSec) {
        this.windowInSec = windowInSec;
    }

    public boolean validate(String code, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        int codeLength = code.length();
        Hmac hmac = new Hmac(secret);
        Hotp hotp = new Hotp(hmac, codeLength);
        long counter = new Date().getTime() / windowInSec * 1000;
        // todo check several periods
        return hotp.generateString(secret, (int) counter).equals(secret);
    }

}
