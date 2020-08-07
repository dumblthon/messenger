package com.dumblthon.messenger.auth.model;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("unused")
@Entity
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name="user_id_generator", sequenceName = "user_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "secret") // for TOTP: updatable = false, nullable = false
    private String secret;

    public UserInfo() {

    }

    public UserInfo(String username) {
        this.username = username;
    }

    public UserInfo(String username, String secret) {
        this.username = username;
        this.secret = secret;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getSecret() {
        return secret;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}
