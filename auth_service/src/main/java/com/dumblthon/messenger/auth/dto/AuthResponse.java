package com.dumblthon.messenger.auth.dto;

import com.dumblthon.messenger.auth.component.OtpSender;
import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private final long userId;
    private final String secret;
    private final String senderId;

    @JsonIgnore
    private final boolean created;

    public AuthResponse(User user, boolean created, OtpSender sender) {
        this(user, null, created, sender);
    }

    public AuthResponse(User user, UserSecret secret, boolean created, OtpSender sender) {
        this.userId = user.getId();
        this.secret = secret != null ? secret.getSecret() : null;
        this.created = created;
        this.senderId = sender.getId();
    }

    public boolean isCreated() {
        return created;
    }

    public long getUserId() {
        return userId;
    }

    public String getSecret() {
        return secret;
    }

    public String getSenderId() {
        return senderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthResponse response = (AuthResponse) o;
        return userId == response.userId &&
                created == response.created &&
                Objects.equals(secret, response.secret) &&
                Objects.equals(senderId, response.senderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, secret, senderId, created);
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "userId=" + userId +
                ", senderId='" + senderId + '\'' +
                ", created=" + created +
                '}';
    }
}
