package com.dumblthon.messenger.auth.service.impl;

import com.dumblthon.messenger.auth.component.CodeGenerator;
import com.dumblthon.messenger.auth.component.MacGenerator;
import com.dumblthon.messenger.auth.component.OtpSender;
import com.dumblthon.messenger.auth.component.SecretGenerator;
import com.dumblthon.messenger.auth.dto.*;
import com.dumblthon.messenger.auth.exception.MissingValidationInfoException;
import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import com.dumblthon.messenger.auth.repository.UserRepository;
import com.dumblthon.messenger.auth.repository.UserSecretRepository;
import com.dumblthon.messenger.auth.service.OtpSenderProvider;
import com.dumblthon.messenger.auth.service.OtpService;
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
public class OtpServiceDefaultImpl implements OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceDefaultImpl.class);

    private static final long ONE_MINUTE_IN_MILLIS = 60000;

    @Value("${otp.secret.length:64}")
    private int secretLength;

    @Value("${otp.code.length:6}")
    private int codeLength;

    @Value("${otp.expiration.period:0}")
    private int expirationPeriodInMinutes;

    private final UserRepository userRepository;
    private final UserSecretRepository secretRepository;
    private final UserOtpRepository otpRepository;

    private final OtpSenderProvider otpSenderProvider;

    private final SecretGenerator secretGenerator;
    private final CodeGenerator codeGenerator;
    private final MacGenerator macGenerator;

    @Autowired
    public OtpServiceDefaultImpl(UserRepository userRepository,
                                 UserSecretRepository secretRepository,
                                 UserOtpRepository otpRepository,
                                 OtpSenderProvider otpSenderProvider,
                                 SecretGenerator secretGenerator,
                                 CodeGenerator codeGenerator,
                                 MacGenerator macGenerator) {
        this.userRepository = userRepository;
        this.secretRepository = secretRepository;
        this.otpRepository = otpRepository;
        this.otpSenderProvider = otpSenderProvider;
        this.secretGenerator = secretGenerator;
        this.codeGenerator = codeGenerator;
        this.macGenerator = macGenerator;
    }

    @Override
    @Transactional
    public AuthResponse authenticate(final AuthRequest authRequest) {
        Optional<User> userOpt = userRepository.findByPhoneNumber(authRequest.getPhoneNumber());

        Long userIdOpt = userOpt.map(User::getId).orElse(null);
        OtpSender otpSender = otpSenderProvider.get(userIdOpt, authRequest.getDeviceId());

        User user = userOpt.orElseGet(() -> userRepository.save(new User(authRequest.getPhoneNumber())));

        Optional<UserSecret> secretOpt = secretRepository.findByUserIdAndDeviceId(user.getId(), authRequest.getDeviceId());
        UserSecret userSecret = secretOpt.orElseGet(() -> createSecret(user.getId(), authRequest.getDeviceId()));

        UserOtp userOtp = createOrUpdateOtp(user.getId(), authRequest.getDeviceId());

        otpSender.send(user, userOtp);

        return secretOpt.isPresent() ?
                new AuthResponse(user, !userOpt.isPresent(), otpSender) :
                new AuthResponse(user, userSecret, !userOpt.isPresent(), otpSender);
    }

    private UserSecret createSecret(long userId, String deviceId) {
        String secret = secretGenerator.generate(secretLength);
        UserSecret userSecret = new UserSecret(userId, deviceId, secret);
        return secretRepository.save(userSecret);
    }

    private UserOtp createOrUpdateOtp(long userId, String deviceId) {
        String otp = codeGenerator.generate(codeLength);
        UserOtp userOtp = new UserOtp(userId, deviceId, otp, new Date());
        return otpRepository.save(userOtp);
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
