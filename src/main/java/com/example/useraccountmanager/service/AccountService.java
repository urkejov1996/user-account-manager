package com.example.useraccountmanager.service;

import com.example.useraccountmanager.dto.request.AccountRequest;
import com.example.useraccountmanager.dto.response.AccountResponse;
import com.example.useraccountmanager.dto.response.UserResponse;
import com.example.useraccountmanager.model.Account;
import com.example.useraccountmanager.model.User;
import com.example.useraccountmanager.repository.AccountRepository;
import com.example.useraccountmanager.repository.UserRepository;
import com.example.useraccountmanager.tools.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

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

    public ResponseEntity<?> createAccount(AccountRequest accountRequest, BindingResult bindingResult) {
        AccountResponse accountResponse = new AccountResponse();
        if (bindingResult.hasErrors()) {
            AccountResponse finalAccountResponse = accountResponse;
            bindingResult.getAllErrors().forEach(error -> {
                finalAccountResponse.addError(error.getDefaultMessage());
            });
            return new ResponseEntity<>(accountResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            if (accountRequest.getUserId().isEmpty() || accountRequest.getUserId().isBlank()) {
                accountResponse.addError(ErrorMessage.BAD_REQUEST);
                return new ResponseEntity<>(accountResponse, HttpStatus.BAD_REQUEST);
            }
            Optional<User> optionalUser = userRepository.findById(accountRequest.getUserId());
            if (optionalUser.isEmpty()) {
                accountResponse.addError(ErrorMessage.NOT_FOUND);
                return new ResponseEntity<>(accountResponse, HttpStatus.NOT_FOUND);
            }
            Account account = new Account();
            account.setUser(optionalUser.get());
            account.setBalance(accountRequest.getBalance());
            accountRepository.save(account);
            accountResponse = mapToAccountDto(account);
            return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("An error occurred while creating the account with userId: {}", accountRequest.getUserId(), e);
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
