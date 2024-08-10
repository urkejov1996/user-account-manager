package com.example.useraccountmanager.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Integer phoneNumber;
    private String address;
    private Set<String> accountIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime lastLoginDate;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> info = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<UserResponse> data = new ArrayList<>();

    public void addInfo(String info) {
        if (this.info == null) {
            this.info = new ArrayList<>();
        }
        this.info.add(info);
    }

    public void addError(String error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
}
