package com.dumblthon.messenger.auth.otp.totp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;

@Entity
public class UserSecret {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_generator")
//    @SequenceGenerator(name="author_generator", sequenceName = "author_seq") allocationSize=1
    @Column(name = "id", updatable = false, nullable = false)
//    @ForeignKey
    private long id;

    @Column(name = "secret", updatable = false, nullable = false)
    private String secret;

    public UserSecret() {}

    public UserSecret(long id, String secret) {
        this.id = id;
        this.secret = secret;
    }

    public long getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }
}
