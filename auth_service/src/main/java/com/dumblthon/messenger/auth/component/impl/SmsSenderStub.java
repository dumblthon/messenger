package com.dumblthon.messenger.auth.component.impl;

import com.dumblthon.messenger.auth.component.OtpSender;
import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserOtp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("smsSenderStub")
public class SmsSenderStub implements OtpSender {

    private static final Logger logger = LoggerFactory.getLogger(MessengerSenderStub.class);

    @Override
    public String getId() {
        return "SMS_STUB";
    }

    @Override
    public void send(User userInfo, UserOtp userOtp) {
        logger.info("One-time password has been sent to userId={} via SMS({})",
                userInfo.getId(), userInfo.getPhoneNumber());
    }

}
