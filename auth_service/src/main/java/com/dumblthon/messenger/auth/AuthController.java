package com.dumblthon.messenger.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class AuthController {

    // replace with response code + message

    private final String SUCCESS = "Entered Otp is valid";

    private final String FAIL = "Entered Otp is NOT valid. Please Retry!";

    @Autowired
    public OtpService otpService;

    // use abstract sender

    @Autowired
    public EmailOtpService myEmailService;

    @GetMapping("/generateOtp")
    public void generateOtp() throws NoSuchAlgorithmException, InvalidKeyException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        int otp = otpService.generateOTP(username);
        // extract info from db
        String email = "";
        myEmailService.sendOtpMessage(email, "Auth Code", Integer.toString(otp));
    }

    @RequestMapping(value = "/validateOtp", method = RequestMethod.GET)
    public String validateOtp(@RequestParam("otpnum") String otp) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (otp != null && otp.equals(otpService.getOtp(username))) {
            otpService.clearOTP(username);
            return SUCCESS;
        }

        return FAIL;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            otpService.clearOTP(username);
            new SecurityContextLogoutHandler()
                    .logout(request, response, auth);
        }
    }

}
