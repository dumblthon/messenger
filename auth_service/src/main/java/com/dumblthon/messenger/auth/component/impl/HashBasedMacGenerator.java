package com.dumblthon.messenger.auth.component.impl;

import com.dumblthon.messenger.auth.component.MacGenerator;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * HMAC (hash-based message authentication code)
 */
@Component
public class HashBasedMacGenerator implements MacGenerator {

    private final Mac mac;

    private static final String HMAC_SHA512 = "HmacSHA512";

    public HashBasedMacGenerator() throws NoSuchAlgorithmException {
        this(HMAC_SHA512);
    }

    public HashBasedMacGenerator(String hashAlgorithm) throws NoSuchAlgorithmException {
        this.mac = Mac.getInstance(HMAC_SHA512);
    }

    @Override
    public String generate(String message, String secret) throws InvalidKeyException {
        final byte[] byteKey = secret.getBytes(StandardCharsets.UTF_8);
        mac.init(new SecretKeySpec(byteKey, mac.getAlgorithm()));
        byte[] tag = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(tag);
    }

}
