package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.component.MacGenerator;
import com.dumblthon.messenger.auth.dto.*;
import com.dumblthon.messenger.auth.exception.MissingValidationInfoException;
import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import com.dumblthon.messenger.auth.repository.UserSecretRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidKeyException;
import java.util.Date;
import java.util.Optional;

@Service
public class OtpValidator {

    private static final Logger logger = LoggerFactory.getLogger(OtpValidator.class);

    private static final long ONE_MINUTE_IN_MILLIS = 60000;

    @Value("${otp.expiration.period:0}")
    private int expirationPeriodInMinutes;

    private final UserSecretRepository secretRepository;
    private final UserOtpRepository otpRepository;

    private final MacGenerator macGenerator;

    @Autowired
    public OtpValidator(UserSecretRepository secretRepository,
                        UserOtpRepository otpRepository,
                        MacGenerator macGenerator) {
        this.secretRepository = secretRepository;
        this.otpRepository = otpRepository;
        this.macGenerator = macGenerator;
    }

    @Transactional
    public ValidationResponse validate(ValidationRequest otpRequest) {
        long userId = otpRequest.getUserId();
        String deviceId = otpRequest.getDeviceId();

        logger.info("One-time password validation for userId={}, deviceId={}", userId, deviceId);

        Optional<UserSecret> secretOpt = secretRepository.findByUserIdAndDeviceId(userId, deviceId);
        Optional<UserOtp> otpOpt = otpRepository.findByUserIdAndDeviceId(userId, deviceId);

        if (secretOpt.isPresent() && otpOpt.isPresent())
            return validate(secretOpt.get(), otpOpt.get());

        if ( !secretOpt.isPresent() )
            logger.error("Validation failed! Missing secret for userId={}, deviceId={}", userId, deviceId);

        if ( !otpOpt.isPresent() )
            logger.error("Validation failed! Missing password for userId={}, deviceId={}", userId, deviceId);

        throw new MissingValidationInfoException(userId, deviceId);
    }

    public ValidationResponse validate(UserSecret userSecret, UserOtp userOtp) {
        if (isExpired(userOtp.getSentAt()))
            return new ValidationFailureResponse(userOtp.getUserId(),
                    userOtp.getDeviceId(), ValidationFailureReason.EXPIRED);

        try {
            String generatedCode = macGenerator.generate(
                    userOtp.getCode(), userSecret.getSecret());
            boolean valid = generatedCode.equals(userOtp.getCode());
            return valid ?
                new ValidationSuccessResponse(
                        userOtp.getUserId(), userOtp.getDeviceId()) :
                new ValidationFailureResponse(
                        userOtp.getUserId(), userOtp.getDeviceId(),
                        ValidationFailureReason.INCORRECT_CODE);
        } catch (InvalidKeyException e) {
            logger.error("Validation failed!", e);
            return new ValidationFailureResponse(userOtp.getUserId(),
                    userOtp.getDeviceId(), ValidationFailureReason.INTERNAL_ERROR);
        }
    }

    private boolean isExpired(Date date) {
        if (expirationPeriodInMinutes <= 0)
            return false;

        long expirationPeriodInMillis = expirationPeriodInMinutes * ONE_MINUTE_IN_MILLIS;
        return new Date(date.getTime() + expirationPeriodInMillis).after(new Date());
    }

}
