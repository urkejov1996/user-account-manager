package com.example.useraccountmanager.dto.request;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class AccountRequest {

    private String userId;
    private BigDecimal balance;
}
