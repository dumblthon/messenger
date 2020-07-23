package com.dumblthon.messenger.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class UserInfo implements Serializable {

    @NonNull
    private final String username;

    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "second_name")
    private String secondName;
}
