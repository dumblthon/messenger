package com.dumblthon.messenger.auth.dto;

import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private final User user;
    private UserSecret userSecret;

    @JsonIgnore
    private final boolean created;

    public AuthResponse(User user, boolean created) {
        this.user = user;
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public boolean isCreated() {
        return created;
    }

    public UserSecret getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(UserSecret userSecret) {
        this.userSecret = userSecret;
    }
}
