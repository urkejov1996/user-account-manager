package com.example.useraccountmanager.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotNull
    private String userId;
    @NotNull
    private BigDecimal balance;

}
