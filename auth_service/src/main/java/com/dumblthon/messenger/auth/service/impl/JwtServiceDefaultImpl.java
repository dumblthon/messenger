package com.dumblthon.messenger.auth.service.impl;

import com.dumblthon.messenger.auth.dto.JwtResponse;
import com.dumblthon.messenger.auth.exception.MissingValidationInfoException;
import com.dumblthon.messenger.auth.exception.UserNotFoundException;
import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import com.dumblthon.messenger.auth.repository.UserRepository;
import com.dumblthon.messenger.auth.service.JwtService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

@Service
public class JwtServiceDefaultImpl implements JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceDefaultImpl.class);

    public static final String USER_ID = "userId";
    public static final String DEVICE_ID = "deviceId";

    @Value("${jwt.issuer.url}")
    private String issuer;

    @Value("${jwt.ttl}")
    private int ttsInSec;

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;

    private final RSAKey rsaKey;

    public JwtServiceDefaultImpl(UserRepository userRepository,
                                 UserOtpRepository userOtpRepository, RSAKey rsaKey) {
        this.userRepository = userRepository;
        this.userOtpRepository = userOtpRepository;
        this.rsaKey = rsaKey;
    }

    @Override
    public JWKSet getJwkSet() {
        return new JWKSet(rsaKey.toPublicJWK());
    }

    @Override
    public JwtResponse generateToken(long userId, String deviceId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        UserOtp userOtp = userOtpRepository.findByUserIdAndDeviceId(userId, deviceId)
                .orElseThrow(() -> new MissingValidationInfoException(userId, deviceId));

        try {
            return new JwtResponse(user, generateToken(user, userOtp));
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private String generateToken(User user, UserOtp userOtp) throws JOSEException {
        JWSHeader jwtHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .jwk(rsaKey)
                //.keyID(rsaKey.getKeyID())
                .build();

        Calendar now = Calendar.getInstance();
        Date issueTime = now.getTime();
        now.add(Calendar.SECOND, ttsInSec);
        Date expiryTime = now.getTime();
        String jti = String.valueOf(issueTime.getTime());

        JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(user.getPhoneNumber())
                .issueTime(issueTime)
                .expirationTime(expiryTime)
                .claim(USER_ID, user.getId())
                .claim(DEVICE_ID, userOtp.getDeviceId())
                .jwtID(jti) // (UUID.randomUUID().toString())
                .build();

        SignedJWT jws = new SignedJWT(jwtHeader, jwtClaims);
        RSASSASigner signer = new RSASSASigner(rsaKey);
        jws.sign(signer);

        return jws.serialize();
    }

    private boolean isValid(HttpServletRequest request) throws ParseException, JOSEException {
        String token = extractToken(request);
        SignedJWT jws = SignedJWT.parse(token);
        JWSVerifier verifier = new RSASSAVerifier(rsaKey.toPublicJWK());
        boolean verified = jws.verify(verifier);

        if(verified) {
            JWTClaimsSet claims = jws.getJWTClaimsSet();
            Date expiryTime = claims.getExpirationTime();

            if(expiryTime.after(new Date())) {
                Long userId = claims.getLongClaim(USER_ID);
                String deviceId = claims.getStringClaim(DEVICE_ID);
                log.info("Token validated for userId = " + userId + ", deviceId = " + deviceId);
                return true;
            }
        }
        return false;
    }

    private String extractToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements()) {
            String key = headers.nextElement();
            if(key.trim().equalsIgnoreCase(HttpHeaders.AUTHORIZATION)) {
                String authorizationHeader = request.getHeader(key);
                if( !authorizationHeader.isEmpty() ) {
                    String[] tokenData = authorizationHeader.split(" ");
                    if(tokenData.length == 2 &&
                            tokenData[0].trim().equalsIgnoreCase("Bearer")) {
                        return tokenData[1];
                    }
                }
            }
        }
        throw new RuntimeException("No token found");
    }

}
