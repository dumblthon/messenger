package com.dumblthon.messenger.auth.controller;

import com.dumblthon.messenger.auth.dto.UserOtp;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.service.TotpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "/api/v1/auth/totp")
public class TotpController {

    private final TotpService totpService;

    @Autowired
    public TotpController(TotpService totpService) {
        this.totpService = totpService;
    }

    @PutMapping(path = "/secret", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserSecret generateSecret(@RequestParam("userId") long userId) {
        return totpService.generateSecret(userId);
    }

    @PostMapping(path = "/validate")
    public void validatePassword(@RequestBody UserOtp userOtp) {
        boolean isValid = totpService.validatePassword(userOtp.getUserId(), userOtp.getCode());
    }

}
