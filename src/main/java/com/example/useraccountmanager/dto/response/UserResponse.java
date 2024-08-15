package com.example.useraccountmanager.dto.response;

import com.example.useraccountmanager.tools.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    private UserStatusEnum status;

    private List<String> info = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
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
