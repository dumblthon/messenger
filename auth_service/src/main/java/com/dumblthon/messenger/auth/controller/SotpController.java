package com.dumblthon.messenger.auth.controller;

import com.dumblthon.messenger.auth.MutableResponse;
import com.dumblthon.messenger.auth.dto.JwtResponse;
import com.dumblthon.messenger.auth.dto.OtpValidationRequest;
import com.dumblthon.messenger.auth.dto.UserAuthRequest;
import com.dumblthon.messenger.auth.model.UserInfo;
import com.dumblthon.messenger.auth.service.JwtService;
import com.dumblthon.messenger.auth.service.SotpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@CrossOrigin // ?
@RequestMapping(path = "/api/v1/auth/sotp")
public class SotpController {

    private final SotpService sotpService;
    private final JwtService jwtService;

    @Autowired
    public SotpController(SotpService sotpService,
                          JwtService jwtService) {
        this.sotpService = sotpService;
        this.jwtService = jwtService;
    }

    @Transactional
    @PostMapping(path = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody UserAuthRequest authRequest) {
        HttpStatus status = sotpService.findUser(authRequest.getUsername()).isPresent() ?
                HttpStatus.OK : HttpStatus.CREATED;

        UserInfo userInfo = sotpService.authenticate(authRequest.getUsername());
        return MutableResponse.create(status)
                .add("userId", userInfo.getId())
                .add("username", userInfo.getUsername())
//                .add("message", "CODE_SENT")
                .get();

    }

    @PostMapping(path = "/validate")
    public ResponseEntity<?> validatePassword(@RequestBody OtpValidationRequest otp) {
        boolean isValid = sotpService.validatePassword(otp.getUserId(), otp.getCode());
        if (isValid) {
            UserInfo userInfo = sotpService.findUser(otp.getUserId()).get();
            String token = jwtService.generateToken(userInfo.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            return MutableResponse.error(HttpStatus.UNAUTHORIZED, "INVALID_CODE")
                    .get();
        }
    }

}
