package com.example.useraccountmanager.dto.request;

import com.example.useraccountmanager.tools.UserStatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@Builder
@RequiredArgsConstructor
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
    private String password;
    @NotNull
    private Integer phoneNumber;
    @NotNull
    private String address;
    @NotNull
    private UserStatusEnum status;
    private Set<AccountRequest> accountRequests;
}
