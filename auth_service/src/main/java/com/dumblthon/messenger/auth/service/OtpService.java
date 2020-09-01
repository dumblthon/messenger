package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.dto.AuthRequest;
import com.dumblthon.messenger.auth.dto.AuthResponse;
import com.dumblthon.messenger.auth.dto.ValidationRequest;
import com.dumblthon.messenger.auth.dto.ValidationResponse;

public interface OtpService {

    AuthResponse authenticate(AuthRequest authRequest);

    ValidationResponse validate(ValidationRequest otpRequest);

}
