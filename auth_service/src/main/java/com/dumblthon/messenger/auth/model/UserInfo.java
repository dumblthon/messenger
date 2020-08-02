package com.dumblthon.messenger.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class UserInfo implements Serializable {

    private final String username;

    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "second_name")
    private String secondName;

    public UserInfo(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return username.equals(userInfo.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
