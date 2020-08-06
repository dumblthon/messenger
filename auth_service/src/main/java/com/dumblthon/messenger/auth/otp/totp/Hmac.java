package com.dumblthon.messenger.auth.otp.totp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// hash-based message authentication codes
public class Hmac {

    private static final String HMAC_SHA512 = "HmacSHA512";

    private String key;

    public Hmac(String key) {
        this.key = key;
    }

    public byte[] generate(String msg, int counter) throws NoSuchAlgorithmException, InvalidKeyException {
        final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
        Mac sha512Hmac = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
        sha512Hmac.init(keySpec);
        return sha512Hmac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
    }

}
