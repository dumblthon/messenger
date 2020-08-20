package com.dumblthon.messenger.auth.model;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String phoneNumber;

    public User(String phoneNumber) {
        this(null, phoneNumber);
    }

    public User(Long id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
