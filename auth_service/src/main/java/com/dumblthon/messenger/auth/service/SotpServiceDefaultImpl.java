package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.model.UserInfo;
import com.dumblthon.messenger.auth.repository.UserInfoRepository;
import com.dumblthon.messenger.auth.service.sotp.sender.OtpSender;
import com.dumblthon.messenger.auth.service.sotp.storage.OtpStorage;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class SotpServiceDefaultImpl implements SotpService {

    private final UserInfoRepository userInfoRepository;
    private final OtpSender otpSender;
    private final OtpStorage otpStorage;

    @Autowired
    public SotpServiceDefaultImpl(UserInfoRepository userInfoRepository,
                                  OtpSender otpSender, OtpStorage otpStorage) {
        this.userInfoRepository = userInfoRepository;
        this.otpSender = otpSender;
        this.otpStorage = otpStorage;
    }

    @Override
    public Optional<UserInfo> findUser(long userId) {
        return userInfoRepository.findById(userId);
    }

    @Override
    public Optional<UserInfo> findUser(String username) {
        return userInfoRepository.findByUsername(username);
    }

    @Override
    public UserInfo authenticate(String username) {
        Optional<UserInfo> userInfoOpt = userInfoRepository.findByUsername(username);
        UserInfo userInfo = userInfoOpt
                .orElseGet(() -> userInfoRepository.save(new UserInfo(username)));

        String code = generateCode();
        otpStorage.save(username, code);
        otpSender.send(username, code);
        return userInfo;
    }

    @Override
    public boolean validatePassword(long userId, String code) {
        Optional<UserInfo> userInfoOpt = userInfoRepository.findById(userId);
        if (userInfoOpt.isPresent() &&
                code.equals(otpStorage.get(userInfoOpt.get().getUsername()))) {
            otpStorage.invalidate(userInfoOpt.get().getUsername());
            return true;
        }
        return false;
    }

    // as service
    private String generateCode() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return Strings.padStart(Integer.toString(otp), 6, '0');
    }

}
