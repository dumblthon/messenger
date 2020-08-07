package com.dumblthon.messenger.auth.service.sotp.sender;

public class OtpSenderStub implements OtpSender {

    @Override
    public boolean send(String username, String code) {
        return true;
    }

}
