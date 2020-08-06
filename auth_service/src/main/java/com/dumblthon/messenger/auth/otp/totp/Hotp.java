package com.dumblthon.messenger.auth.otp.totp;

import com.google.common.base.Strings;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

// HMAC-based One-time Password algorithm
public class Hotp {

    private static final int DEFAULT_LENGTH = 6;

    private int digits;
    private Hmac hmac;

    public Hotp(Hmac hmac) {
        this(hmac, DEFAULT_LENGTH);
    }

    public Hotp(Hmac hmac, int digits) {
        this.hmac = hmac;
        this.digits = digits;
    }

    public String generateString(String key, int counter) throws InvalidKeyException, NoSuchAlgorithmException {
        int intCode = generateInt(key, counter);
        return Strings.padStart(Integer.toString(intCode), digits, '0');
    }

    public int generateInt(String key, int counter) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] generatedHmac = hmac.generate(key, counter);
        int offset = generatedHmac[generatedHmac.length - 1] & 0xf;
        int binary =
                ((generatedHmac[offset] & 0x7f) << 24) |
                ((generatedHmac[offset + 1] & 0xff) << 16) |
                ((generatedHmac[offset + 2] & 0xff) << 8) |
                (generatedHmac[offset + 3] & 0xff);
        return binary % ((int) Math.pow(10, digits));
    }

}
