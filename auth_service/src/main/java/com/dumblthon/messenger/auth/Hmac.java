package com.dumblthon.messenger.auth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hmac {

    private static final String HMAC_SHA512 = "HmacSHA512";

    private String key;

    public Hmac(String key) {
        this.key = key;
    }

    public String generate(String msg) throws NoSuchAlgorithmException, InvalidKeyException {
        final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
        Mac sha512Hmac = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
        sha512Hmac.init(keySpec);
        byte[] macData = sha512Hmac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(macData);
    }

}
