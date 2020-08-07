package com.dumblthon.messenger.auth.service;

import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    String generateToken(String username);

    boolean validateToken(String token);

}
