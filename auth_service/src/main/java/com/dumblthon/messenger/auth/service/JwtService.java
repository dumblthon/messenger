package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.dto.JwtResponse;

public interface JwtService {

    JwtResponse generateToken(long userId, String deviceId);

}
