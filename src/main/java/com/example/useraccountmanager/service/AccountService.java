package com.example.useraccountmanager.service;

import com.example.useraccountmanager.dto.response.AccountResponse;
import com.example.useraccountmanager.model.Account;
import com.example.useraccountmanager.repository.AccountRepository;
import com.example.useraccountmanager.tools.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public ResponseEntity<?> getAccount(String userId) {
        AccountResponse accountResponse = new AccountResponse();
        try {
            if (userId.isEmpty() || userId.isBlank()) {
                accountResponse.addError(ErrorMessage.BAD_REQUEST);
                return new ResponseEntity<>(accountResponse, HttpStatus.BAD_REQUEST);
            }
            Optional<Account> optionalAccount = accountRepository.findByUserId(userId);
            if (optionalAccount.isEmpty()) {
                accountResponse.addError(ErrorMessage.NOT_FOUND);
                return new ResponseEntity<>(accountResponse, HttpStatus.NOT_FOUND);
            }
            Account account = optionalAccount.get();
            accountResponse = mapToAccountDto(account);
            return new ResponseEntity<>(accountResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while retrieving the account with userId: {}", userId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private AccountResponse mapToAccountDto(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .userId(account.getUser().getId())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .deletedAt(account.getDeletedAt())
                .build();
    }
}
