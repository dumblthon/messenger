package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.dto.JwtResponse;
import com.dumblthon.messenger.auth.exception.MissingValidationInfoException;
import com.dumblthon.messenger.auth.exception.UserNotFoundException;
import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import com.dumblthon.messenger.auth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceDefaultImpl implements JwtService {

    public static final String DEVICE_ID = "deviceId";
    public static final String AUTH_CODE = "authCode";

    @Value("${jwt.secret}")
    private String secret;

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

        String token = Jwts.builder()
                .setSubject(user.getPhoneNumber())
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ttsInSec * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return new JwtResponse(user, userOtp.getDeviceId(), token);
    }

}
