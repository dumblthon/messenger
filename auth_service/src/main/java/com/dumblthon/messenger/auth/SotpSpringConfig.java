package com.dumblthon.messenger.auth;

import com.dumblthon.messenger.auth.repository.UserInfoRepository;
import com.dumblthon.messenger.auth.service.SotpService;
import com.dumblthon.messenger.auth.service.SotpServiceDefaultImpl;
import com.dumblthon.messenger.auth.service.sotp.sender.OtpSender;
import com.dumblthon.messenger.auth.service.sotp.sender.OtpSenderStub;
import com.dumblthon.messenger.auth.service.sotp.storage.CacheOtpStorage;
import com.dumblthon.messenger.auth.service.sotp.storage.OtpStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("unused")
@Configuration
public class SotpSpringConfig {

    @Value("${sotp.expireMins:5}")
    private int expireMinutes;

    @Bean
    public OtpStorage otpStorage() {
        return new CacheOtpStorage(expireMinutes);
    }

    @Bean
    public OtpSender otpSender() {
        return new OtpSenderStub();
    }

    @Bean
    public SotpService sotpService(UserInfoRepository userInfoRepository,
                                   OtpSender otpSender, OtpStorage otpStorage) {
        return new SotpServiceDefaultImpl(userInfoRepository, otpSender, otpStorage);
    }

}
