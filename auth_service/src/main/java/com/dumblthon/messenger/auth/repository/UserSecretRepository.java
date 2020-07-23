package com.dumblthon.messenger.auth.repository;

import com.dumblthon.messenger.auth.model.UserSecret;

import java.util.Optional;

public interface UserSecretRepository {

    Optional<UserSecret> findByUserIdAndDeviceId(long userId, String deviceId);

    UserSecret save(UserSecret secret);

}
