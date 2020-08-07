package com.dumblthon.messenger.auth.service.sotp.sender;

import org.springframework.stereotype.Service;

@Service
public interface OtpSender {

    boolean send(String username, String code);

}
