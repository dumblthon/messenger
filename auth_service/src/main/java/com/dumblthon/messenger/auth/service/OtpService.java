package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.dto.AuthRequest;
import com.dumblthon.messenger.auth.dto.AuthResponse;

public interface OtpService {

    AuthResponse authenticate(AuthRequest authRequest);

}
