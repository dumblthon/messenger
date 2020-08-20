package com.dumblthon.messenger.auth.controller;

import com.dumblthon.messenger.auth.dto.*;
import com.dumblthon.messenger.auth.service.JwtService;
import com.dumblthon.messenger.auth.service.OtpService;
import com.dumblthon.messenger.auth.service.OtpValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
public class AuthController {

    private final OtpService otpService;
    private final OtpValidator otpValidator;
    private final JwtService jwtService;

    @Autowired
    public AuthController(OtpService otpService,
                          OtpValidator otpValidator,
                          JwtService jwtService) {
        this.otpService = otpService;
        this.otpValidator = otpValidator;
        this.jwtService = jwtService;
    }

    @Transactional
    @PostMapping(path = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        AuthResponse response = otpService.authenticate(authRequest);
        HttpStatus status = response.isCreated() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(path = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validate(@RequestBody ValidationRequest otpRequest) {
        ValidationResponse validationResponse = otpValidator.validate(otpRequest);

        if (validationResponse.isSuccessful()) {
            JwtResponse response = jwtService.generateToken(
                    otpRequest.getUserId(), otpRequest.getDeviceId());
            return ResponseEntity.ok(response);
        } else {
            ValidationFailureResponse failureResponse = (ValidationFailureResponse) validationResponse;
            HttpStatus status = failureResponse.getReason() == ValidationFailureReason.INTERNAL_ERROR ?
                    HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.UNAUTHORIZED;
            return ResponseEntity.status(status).body(validationResponse);
        }
    }
}
