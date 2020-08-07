package com.dumblthon.messenger.auth.controller;

import com.dumblthon.messenger.auth.MutableResponse;
import com.dumblthon.messenger.auth.dto.JwtResponse;
import com.dumblthon.messenger.auth.dto.OtpValidationRequest;
import com.dumblthon.messenger.auth.dto.UserAuthRequest;
import com.dumblthon.messenger.auth.model.UserInfo;
import com.dumblthon.messenger.auth.service.JwtService;
import com.dumblthon.messenger.auth.service.TotpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SuppressWarnings("unused")
@RestController
@CrossOrigin // ?
@RequestMapping(path = "/api/v1/auth/totp")
public class TotpController {

    private final TotpService totpService;
    private final JwtService jwtService;

    @Autowired
    public TotpController(TotpService totpService,
                          JwtService jwtService) {
        this.totpService = totpService;
        this.jwtService = jwtService;
    }

    @Transactional
    @PostMapping(path = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody UserAuthRequest authRequest) {
        Optional<UserInfo> foundUser = totpService.findUser(authRequest.getUsername());
        if (foundUser.isPresent()) {
            UserInfo userInfo = foundUser.get();
            return MutableResponse.create(HttpStatus.OK)
                    .add("userId", userInfo.getId())
                    .add("username", userInfo.getUsername())
                    .get();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(totpService.createUser(authRequest.getUsername()));
    }

    @PostMapping(path = "/validate")
    public ResponseEntity<?> validatePassword(@RequestBody OtpValidationRequest otp) {
        boolean isValid = totpService.validatePassword(otp.getUserId(), otp.getCode());
        if (isValid) {
            UserInfo userInfo = totpService.findUser(otp.getUserId()).get();
            String token = jwtService.generateToken(userInfo.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            return MutableResponse.error(HttpStatus.UNAUTHORIZED, "INVALID_CODE")
                    .get();
        }
    }

}
