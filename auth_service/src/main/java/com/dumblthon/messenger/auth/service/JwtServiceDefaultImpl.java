package com.dumblthon.messenger.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;

public class JwtServiceDefaultImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.ttl}")
    private int ttsInSec;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setClaims(new HashMap<>())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ttsInSec * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        // check claims.getIssuer()
        // check claims.getSubject()
        return claims.getExpiration().after(new Date()); // not expired
    }

}
