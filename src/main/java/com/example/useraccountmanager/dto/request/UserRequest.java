package com.example.useraccountmanager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String username;
    @Email
    private String email;
    @NotNull
    private Integer phoneNumber;
    @NotNull
    private String address;
    private Set<AccountRequest> accountRequests;
}
