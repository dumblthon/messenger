package com.dumblthon.messenger.auth.controller;

import com.dumblthon.messenger.auth.component.impl.MessengerSenderStub;
import com.dumblthon.messenger.auth.component.impl.SmsSenderStub;
import com.dumblthon.messenger.auth.dto.*;
import com.dumblthon.messenger.auth.exception.MissingValidationInfoException;
import com.dumblthon.messenger.auth.exception.UserNotFoundException;
import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.service.JwtService;
import com.dumblthon.messenger.auth.service.OtpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OtpService otpService;

    @MockBean
    private JwtService jwtService;

    @Test
    public void testAuthenticateExistingUser() throws Exception {
        User user = new User(1L, "ignored");
        AuthResponse response = new AuthResponse(user, false, new SmsSenderStub());
        testAuthenticate(response);
    }

    @Test
    public void testAuthenticateNewUser() throws Exception {
        User user = new User(1L, "ignored");
        UserSecret secret = new UserSecret(1L, "1", "secret");
        AuthResponse response = new AuthResponse(user, secret, true, new MessengerSenderStub());
        testAuthenticate(response);
    }

    private void testAuthenticate(AuthResponse response) throws Exception {
        when(otpService.authenticate(any())).thenReturn(response);

        AuthRequest request = new AuthRequest();
        request.setDeviceId("1");
        request.setPhoneNumber("78945671234");

        this.mockMvc
                .perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(response.isCreated() ? status().isCreated() : status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void testAuthEmptyDeviceId() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setPhoneNumber("78945671234");
        testAuthBadRequest(request);
    }

    @Test
    public void testAuthIncorrectPhoneNumber() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setDeviceId("dev_1");
        request.setPhoneNumber("+1234567890");
        testAuthBadRequest(request);
    }

    private void testAuthBadRequest(AuthRequest request) throws Exception {
        this.mockMvc
                .perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testValidationEmptyDeviceId() throws Exception {
        ValidationRequest request = new ValidationRequest(
                1L, null, "123456");

        this.mockMvc
                .perform(post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testValidationSuccessful() throws Exception {
        ValidationRequest request = new ValidationRequest(1L, "dev_1", "123456");

        ValidationResponse validationResponse = new ValidationSuccessResponse(1L, "dev_1");
        when(otpService.validate(any())).thenReturn(validationResponse);

        JwtResponse jwtResponse = new JwtResponse(new User(1L, "ignored"), "token");
        when(jwtService.generateToken(anyLong(), any())).thenReturn(jwtResponse);

        this.mockMvc
                .perform(post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    @Test
    public void testValidationFailure() throws Exception {
        ValidationRequest request = new ValidationRequest(1L, "dev_1", "123456");

        ValidationResponse validationResponse = new ValidationFailureResponse(
                1L, "dev_1", ValidationFailureReason.EXPIRED);
        when(otpService.validate(any())).thenReturn(validationResponse);

        this.mockMvc
                .perform(post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(objectMapper.writeValueAsString(validationResponse)));
    }

    @Test
    public void testValidateMissingInfo() throws Exception {
        ValidationRequest request = new ValidationRequest(1L, "dev_1", "123456");

        MissingValidationInfoException e = new MissingValidationInfoException(1L, "dev_1");
        when(otpService.validate(any())).thenThrow(e);

        this.mockMvc
                .perform(post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
//                .andExpect(content().json(objectMapper.writeValueAsString(e)));
    }

    @Test
    public void testGenerateTokenUserNotFound() throws Exception {
        ValidationRequest request = new ValidationRequest(1L, "dev_1", "123456");

        when(otpService.validate(any())).thenReturn(new ValidationSuccessResponse(1L, "dev_1"));

        UserNotFoundException e = new UserNotFoundException(1L);
        when(jwtService.generateToken(anyLong(), any())).thenThrow(e);

        this.mockMvc
                .perform(post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
//                .andExpect(content().json(objectMapper.writeValueAsString(e)));
    }

}
