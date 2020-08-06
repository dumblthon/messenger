package com.dumblthon.messenger.auth.otp.totp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSecretRepository extends JpaRepository<UserSecret, Long> {

}
