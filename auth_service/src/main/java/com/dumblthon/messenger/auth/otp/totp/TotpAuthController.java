package com.dumblthon.messenger.auth.otp.totp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class TotpAuthController {

    private final TotpService totpService;

    @Autowired
    public TotpAuthController(TotpService totpService) {
        this.totpService = totpService;
    }

    @PutMapping(path = "/secret", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserSecret generateSecret(@RequestParam("userId") long userId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return totpService.generateSecret(userId);
    }

    @PostMapping(path = "/validate")
    public void validatePassword(@RequestBody UserOtpDto userOtp) {
        // todo test trailing zeros
        boolean isValid = totpService.validatePassword(userOtp.getUserId(), userOtp.getCode());
    }

}
