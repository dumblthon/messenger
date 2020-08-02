package com.dumblthon.messenger.auth;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    // use cache OR database ?

    // cache based on username and OPT MAX 8
    private static final Integer EXPIRE_MINS = 5;

    private final Cache<String, String> otpCache;

    private final String secret;

    @Autowired
    public OtpService(String secret) {
        this.secret = secret;

        this.otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).
                build();
    }

    //This method is used to push the opt number against Key. Rewrite the OTP if it exists
    //Using user id as key
    public int generateOTP(String key) throws InvalidKeyException, NoSuchAlgorithmException {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, new Hmac(secret)
                .generate(Integer.toString(otp)));
        return otp;
    }

    //This method is used to return the OPT number against Key->Key values is username
    public String getOtp(String key) {
        return otpCache.getIfPresent(key);
    }

    //This method is used to clear the OTP catched already
    public void clearOTP(String key) {
        otpCache.invalidate(key);
    }

}
