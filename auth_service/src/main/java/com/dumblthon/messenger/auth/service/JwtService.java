package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.dto.JwtResponse;
import com.nimbusds.jose.jwk.JWKSet;

public interface JwtService {

    JWKSet getJwkSet();

    JwtResponse generateToken(long userId, String deviceId);

}
