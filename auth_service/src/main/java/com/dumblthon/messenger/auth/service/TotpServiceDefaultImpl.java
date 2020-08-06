package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.exception.UserAlreadyExistsException;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.repository.UserSecretRepository;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class TotpServiceDefaultImpl implements TotpService {

    private final SecretGenerator secretGenerator;

    private final CodeVerifier codeVerifier;

    private final UserSecretRepository userSecretRepository;

    @Autowired
    public TotpServiceDefaultImpl(SecretGenerator secretGenerator,
                                  CodeVerifier codeVerifier,
                                  UserSecretRepository userSecretRepository) {
        this.secretGenerator = secretGenerator;
        this.codeVerifier = codeVerifier;
        this.userSecretRepository = userSecretRepository;
    }

    @Override
    @Transactional
    public UserSecret generateSecret(long userId) throws UserAlreadyExistsException {
        Optional<UserSecret> existingRecord = userSecretRepository.findById(userId);
        if (existingRecord.isPresent())
            throw new UserAlreadyExistsException("Ключ уже сгенерирован", userId);
        String secret = secretGenerator.generate();
        UserSecret userSecret = new UserSecret(userId, secret);
        return userSecretRepository.save(userSecret);
    }

    @Override
    public boolean validatePassword(long userId, String code) {
        Optional<UserSecret> existingRecord = userSecretRepository.findById(userId);
        return existingRecord.isPresent() &&
                codeVerifier.isValidCode(existingRecord.get().getSecret(), code);
    }

}
