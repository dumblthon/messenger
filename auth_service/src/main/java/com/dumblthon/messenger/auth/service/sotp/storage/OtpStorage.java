package com.dumblthon.messenger.auth.service.sotp.storage;

import org.springframework.stereotype.Service;

@Service
public interface OtpStorage {

    void save(String username, String code);

    String get(String username);

    void invalidate(String username);

}
