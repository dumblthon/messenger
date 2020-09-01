package com.dumblthon.messenger.auth.service.impl;

import com.dumblthon.messenger.auth.dto.JwtResponse;
import com.dumblthon.messenger.auth.exception.MissingValidationInfoException;
import com.dumblthon.messenger.auth.exception.UserNotFoundException;
import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import com.dumblthon.messenger.auth.repository.UserRepository;
import com.dumblthon.messenger.auth.service.JwtService;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceDefaultImpl implements JwtService {

    public static final String DEVICE_ID = "deviceId";
    public static final String AUTH_CODE = "authCode";

    @Value("${jwt.jks.pass}")
    private String jksPass;

    @Value("${jwt.ttl}")
    private int ttsInSec;

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;

    public JwtServiceDefaultImpl(UserRepository userRepository,
                                 UserOtpRepository userOtpRepository) {
        this.userRepository = userRepository;
        this.userOtpRepository = userOtpRepository;
    }

    @Override
    public JwtResponse generateToken(long userId, String deviceId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        UserOtp userOtp = userOtpRepository.findByUserIdAndDeviceId(userId, deviceId)
                .orElseThrow(() -> new MissingValidationInfoException(userId, deviceId));

        Map<String, Object> claims = new HashMap<>();
        claims.put(DEVICE_ID, userOtp.getDeviceId());
        claims.put(AUTH_CODE, userOtp.getCode());

        return new JwtResponse(user, "");
    }

    public void test() throws KeyStoreException, IOException,
            CertificateException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        char[] password = jksPass.toCharArray();
        //new FileInputStream("myKeyStore.jks", password)
        ClassPathResource ksFile = new ClassPathResource("msg-jwt.jks");
        keyStore.load(ksFile.getInputStream(), password);
        JWKSet jwkSet = JWKSet.load(keyStore, null);
    }

}
