package com.dumblthon.messenger.auth.service.sotp.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

public class CacheOtpStorage implements OtpStorage {

    private final Cache<String, String> cache;

    @Autowired
    public CacheOtpStorage(int expireMinutes) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireMinutes, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void save(String username, String code) {
        cache.put(username, code);
    }

    @Nullable
    @Override
    public String get(String username) {
        return cache.getIfPresent(username);
    }

    @Override
    public void invalidate(String username) {
        cache.invalidate(username);
    }

}
