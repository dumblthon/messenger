package com.dumblthon.messenger.auth.component.impl;

import com.dumblthon.messenger.auth.component.SecretGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Реализация интерфейса для генерации пользовательского ключа с помощью
 * @see java.security.SecureRandom
 */
@Component
public class SecureRandomSecretGenerator implements SecretGenerator {

    public String generate(int length) {
        byte[] buf = new byte[length];
        new SecureRandom().nextBytes(buf);
        String secret = Base64.getEncoder()
                .encodeToString(buf);
        return secret.substring(1, length + 1);
    }

}
