package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.component.CodeGenerator;
import com.dumblthon.messenger.auth.component.MacGenerator;
import com.dumblthon.messenger.auth.component.OtpSender;
import com.dumblthon.messenger.auth.component.SecretGenerator;
import com.dumblthon.messenger.auth.component.impl.MessengerSenderStub;
import com.dumblthon.messenger.auth.component.impl.SmsSenderStub;
import com.dumblthon.messenger.auth.dto.AuthRequest;
import com.dumblthon.messenger.auth.dto.AuthResponse;
import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import com.dumblthon.messenger.auth.repository.UserRepository;
import com.dumblthon.messenger.auth.repository.UserSecretRepository;
import com.dumblthon.messenger.auth.service.impl.OtpSenderProviderStub;
import com.dumblthon.messenger.auth.service.impl.OtpServiceDefaultImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OtpServiceTest {

    @TestConfiguration
    static class OtpServiceTestConfiguration {

        @MockBean
        UserRepository userRepository;
        @MockBean
        UserSecretRepository secretRepository;
        @MockBean
        UserOtpRepository otpRepository;
        @MockBean
        SecretGenerator secretGenerator;
        @MockBean
        CodeGenerator codeGenerator;
        @MockBean
        MacGenerator macGenerator;

        @Bean
        public OtpSenderProvider otpSenderProvider() {
            return new OtpSenderProviderStub(new SmsSenderStub(), new MessengerSenderStub());
        }

        @Bean
        public OtpService otpService() {
            return new OtpServiceDefaultImpl(userRepository, secretRepository, otpRepository,
                    otpSenderProvider(), secretGenerator, codeGenerator, macGenerator);
        }

    }

    @Autowired
    private OtpService otpService;

    @Autowired
    private OtpServiceTestConfiguration conf;

    private static AuthRequest request;

    @BeforeClass
    public static void init() {
        request = new AuthRequest();
        request.setDeviceId("dev_1");
        request.setPhoneNumber("76541093857");
    }

    @Test
    public void testAuthExistingUserNoSecret() {
        User user = new User(1L, request.getPhoneNumber());
        when(conf.userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(user));

        UserOtp userOtp = new UserOtp(1L, request.getDeviceId(), "1234567", new Date());
        when(conf.otpRepository.save(any())).thenReturn(userOtp);

        UserSecret secret = new UserSecret(1L, request.getDeviceId(), "secT_T");
        when(conf.secretRepository.save(any())).thenReturn(secret);
        when(conf.secretRepository.findByUserIdAndDeviceId(anyLong(), any())).thenReturn(Optional.empty());

        when(conf.codeGenerator.generate(anyInt())).thenReturn("1234567");
        when(conf.secretGenerator.generate(anyInt())).thenReturn("secT_T");

        OtpSender sender = new MessengerSenderStub();

        AuthResponse expected = new AuthResponse(user, secret, false, sender);

        Assert.assertEquals(expected, otpService.authenticate(request));
    }

}
