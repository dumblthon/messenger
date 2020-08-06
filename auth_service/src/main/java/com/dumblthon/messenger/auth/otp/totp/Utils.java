package com.dumblthon.messenger.auth.otp.totp;

import java.security.SecureRandom;
import java.util.Base64;

class Utils {

    public static String generateSecret(int length) {
        byte[] buf = new byte[length];
        new SecureRandom().nextBytes(buf);
        String secret = Base64.getEncoder()
                .encodeToString(buf);
        return secret.substring(1, length + 1);
    }

}
