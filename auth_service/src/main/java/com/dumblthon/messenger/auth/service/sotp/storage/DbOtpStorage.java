package com.dumblthon.messenger.auth.service.sotp.storage;

import com.dumblthon.messenger.auth.model.UserInfo;
import com.dumblthon.messenger.auth.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.Optional;

public class DbOtpStorage implements OtpStorage {

    private final UserInfoRepository repository;

    @Autowired
    public DbOtpStorage(UserInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(String username, String code) {
        Optional<UserInfo> userInfoOpt = repository.findByUsername(username);
        if (userInfoOpt.isPresent()) {
            UserInfo userInfo = userInfoOpt.get();
            userInfo.setSecret(code);
            repository.save(userInfo);
        }

        throw new IllegalStateException("Пользователь " + username + " не существует");
    }

    @Nullable
    @Override
    public String get(String username) {
        Optional<UserInfo> userInfoOpt = repository.findByUsername(username);
        return userInfoOpt.map(UserInfo::getSecret).orElse(null);
    }

    @Override
    public void invalidate(String username) {
        Optional<UserInfo> userInfoOpt = repository.findByUsername(username);
        if (userInfoOpt.isPresent()) {
            UserInfo userInfo = userInfoOpt.get();
            userInfo.setSecret(null);
            repository.save(userInfo);
        }
    }

}
