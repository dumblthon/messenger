package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import com.dumblthon.messenger.auth.repository.UserRepository;
import com.dumblthon.messenger.auth.security.JwtConfiguration;
import com.dumblthon.messenger.auth.service.impl.JwtServiceDefaultImpl;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class JwtServiceTest {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceTest.class);

    @TestConfiguration
    @Import(JwtConfiguration.class)
    static class OtpServiceTestConfiguration {

        @MockBean
        UserRepository userRepository;
        @MockBean
        UserOtpRepository otpRepository;

        @Autowired
        RSAKey rsaKey;

        @Bean
        public JwtService jwtService() {
            return new JwtServiceDefaultImpl(userRepository, otpRepository, rsaKey);
        }
    }

    @Autowired
    private JwtService jwtService;

    @Test
    public void test() {
        log.info("{}", jwtService.getJwkSet().toJSONObject(true));
    }

}
