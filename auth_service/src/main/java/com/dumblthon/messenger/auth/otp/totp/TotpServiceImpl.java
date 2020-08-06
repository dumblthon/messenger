package com.dumblthon.messenger.auth.otp.totp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class TotpServiceImpl implements TotpService {

    private final UserSecretRepository userSecretRepository;

    private final TotpValidator totpValidator;

    /**
     * Кол-во символов в сгенерированном секретном ключе
     */
    @Value("${auth.totp.secret.length:20}")
    private int secretLength;

    /**
     * Время в секундах в течение которого пароль действителен
     */
    @Value("${auth.totp.windowInSec:30}")
    private int windowInSec;

    @Autowired
    public TotpServiceImpl(UserSecretRepository userSecretRepository) {
        this.userSecretRepository = userSecretRepository;
        this.totpValidator = new TotpValidator(windowInSec);
    }

    @Override
    @Transactional
    public UserSecret generateSecret(long userId) throws UserAlreadyExistsException {
        Optional<UserSecret> existingRecord = userSecretRepository.findById(userId);
        if (existingRecord.isPresent())
            throw new UserAlreadyExistsException("Ключ уже сгенерирован", userId);
        String secret = Utils.generateSecret(secretLength);
        UserSecret userSecret = new UserSecret(userId, secret);
        return userSecretRepository.save(userSecret);
    }

    @Override
    public boolean validatePassword(long userId, String code) {
        Optional<UserSecret> existingRecord = userSecretRepository.findById(userId);
        try {
            return existingRecord.isPresent() && totpValidator.validate(code, existingRecord.get().getSecret());
        } catch (Exception e) {
            e.printStackTrace();
            // todo return 500
            return false;
        }
    }
}
