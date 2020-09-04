package com.dumblthon.messenger.auth.repository;

import com.dumblthon.messenger.auth.model.UserOtp;

import java.util.Optional;

public interface UserOtpRepository {

    Optional<UserOtp> findByUserIdAndDeviceId(long userId, String deviceId);

    UserOtp save(UserOtp otp);

}
