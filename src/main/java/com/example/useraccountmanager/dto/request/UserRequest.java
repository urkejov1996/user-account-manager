package com.example.useraccountmanager.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDateTime lastLoginDate;
    private Set<Long> accountIds;
}
