package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.component.CodeGenerator;
import com.dumblthon.messenger.auth.component.OtpSender;
import com.dumblthon.messenger.auth.component.SecretGenerator;
import com.dumblthon.messenger.auth.dto.AuthRequest;
import com.dumblthon.messenger.auth.dto.AuthResponse;
import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import com.dumblthon.messenger.auth.repository.UserRepository;
import com.dumblthon.messenger.auth.repository.UserSecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class OtpServiceDefaultImpl implements OtpService {

    @Value("${otp.secret.length:64}")
    private int secretLength;

    @Value("${otp.code.length:6}")
    private int codeLength;

    private final UserRepository userRepository;
    private final UserSecretRepository secretRepository;
    private final UserOtpRepository otpRepository;

    private final OtpSenderProvider otpSenderProvider;

    private final SecretGenerator secretGenerator;
    private final CodeGenerator codeGenerator;

    @Autowired
    public OtpServiceDefaultImpl(UserRepository userRepository,
                                 UserSecretRepository secretRepository,
                                 UserOtpRepository otpRepository,
                                 OtpSenderProvider otpSenderProvider,
                                 SecretGenerator secretGenerator,
                                 CodeGenerator codeGenerator) {
        this.userRepository = userRepository;
        this.secretRepository = secretRepository;
        this.otpRepository = otpRepository;
        this.otpSenderProvider = otpSenderProvider;
        this.secretGenerator = secretGenerator;
        this.codeGenerator = codeGenerator;
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

        AuthResponse response = new AuthResponse(user, userOpt.isPresent());
        if ( !secretOpt.isPresent() )
            response.setUserSecret(userSecret);
        return response;
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

}
