package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.exception.UserAlreadyExistsException;
import com.dumblthon.messenger.auth.model.UserInfo;
import com.dumblthon.messenger.auth.repository.UserInfoRepository;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class TotpServiceDefaultImpl implements TotpService {

    private final SecretGenerator secretGenerator;

    private final CodeVerifier codeVerifier;

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public TotpServiceDefaultImpl(SecretGenerator secretGenerator,
                                  CodeVerifier codeVerifier,
                                  UserInfoRepository userInfoRepository) {
        this.secretGenerator = secretGenerator;
        this.codeVerifier = codeVerifier;
        this.userInfoRepository = userInfoRepository;
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
    @Transactional
    public UserInfo createUser(String username) throws UserAlreadyExistsException {
        String secret = secretGenerator.generate();
        UserInfo userInfo = new UserInfo(username, secret);
        return userInfoRepository.save(userInfo);
    }

    @Override
    public boolean validatePassword(long userId, String code) {
        Optional<UserInfo> existingRecord = userInfoRepository.findById(userId);
        return existingRecord.isPresent() &&
                codeVerifier.isValidCode(existingRecord.get().getSecret(), code);
    }

}
