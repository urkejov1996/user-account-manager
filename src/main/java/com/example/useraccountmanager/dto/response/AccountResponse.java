package com.example.useraccountmanager.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccountResponse {

    private String id;
    private String userId;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<String> info = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    private List<AccountResponse> data = new ArrayList<>();

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
