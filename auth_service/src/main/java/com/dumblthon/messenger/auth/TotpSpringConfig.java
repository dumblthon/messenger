package com.dumblthon.messenger.auth;

import com.dumblthon.messenger.auth.repository.UserInfoRepository;
import com.dumblthon.messenger.auth.service.TotpService;
import com.dumblthon.messenger.auth.service.TotpServiceDefaultImpl;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("unused")
@Configuration
public class TotpSpringConfig {

    @Bean
    public TotpService totpService(SecretGenerator secretGenerator,
                                   CodeVerifier codeVerifier,
                                   UserInfoRepository userInfoRepository) {

        return new TotpServiceDefaultImpl(secretGenerator, codeVerifier, userInfoRepository);
    }

    @Bean
    public HashingAlgorithm hashingAlgorithm() {
        return HashingAlgorithm.SHA256;
    }

}
