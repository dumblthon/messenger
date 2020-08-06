package com.dumblthon.messenger.auth.repository;

import com.dumblthon.messenger.auth.model.UserSecret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSecretRepository extends JpaRepository<UserSecret, Long> {

}
