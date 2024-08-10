package com.example.useraccountmanager.service;

import com.example.useraccountmanager.dto.request.AccountRequest;
import com.example.useraccountmanager.dto.response.AccountResponse;
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

/**
 * Service class for managing accounts. This class provides methods to perform CRUD operations on the Account entity.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves an account by the associated user's ID.
     *
     * @param userId The ID of the user whose account to retrieve.
     * @return ResponseEntity containing the AccountResponse if found, or an error message if not.
     */
    public ResponseEntity<?> getAccount(String userId) {
        AccountResponse accountResponse = new AccountResponse();
        try {
            // Validate userId input
            if (userId.isEmpty() || userId.isBlank()) {
                accountResponse.addError(ErrorMessage.BAD_REQUEST);
                return new ResponseEntity<>(accountResponse, HttpStatus.BAD_REQUEST);
            }

            // Fetch the account associated with the user from the repository
            Optional<Account> optionalAccount = accountRepository.findByUserId(userId);
            if (optionalAccount.isEmpty()) {
                accountResponse.addError(ErrorMessage.NOT_FOUND);
                return new ResponseEntity<>(accountResponse, HttpStatus.NOT_FOUND);
            }

            // Map Account entity to AccountResponse DTO
            Account account = optionalAccount.get();
            accountResponse = mapToAccountDto(account);
            return new ResponseEntity<>(accountResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while retrieving the account with userId: {}", userId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new account.
     *
     * @param accountRequest The AccountRequest DTO containing the new account's data.
     * @param bindingResult The BindingResult object that holds the result of the validation and binding and contains errors that may have occurred.
     * @return ResponseEntity containing the created AccountResponse or error message if the creation fails.
     */
    public ResponseEntity<?> createAccount(AccountRequest accountRequest, BindingResult bindingResult) {
        AccountResponse accountResponse = new AccountResponse();

        // Validate the input request
        if (bindingResult.hasErrors()) {
            AccountResponse finalAccountResponse = accountResponse;
            bindingResult.getAllErrors().forEach(error -> {
                finalAccountResponse.addError(error.getDefaultMessage());
            });
            return new ResponseEntity<>(accountResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            // Validate userId input in the request
            if (accountRequest.getUserId().isEmpty() || accountRequest.getUserId().isBlank()) {
                accountResponse.addError(ErrorMessage.BAD_REQUEST);
                return new ResponseEntity<>(accountResponse, HttpStatus.BAD_REQUEST);
            }

            // Fetch the user associated with the account
            Optional<User> optionalUser = userRepository.findById(accountRequest.getUserId());
            if (optionalUser.isEmpty()) {
                accountResponse.addError(ErrorMessage.NOT_FOUND);
                return new ResponseEntity<>(accountResponse, HttpStatus.NOT_FOUND);
            }

            // Create new Account entity from AccountRequest DTO and associate it with the user
            Account account = new Account();
            account.setUser(optionalUser.get());
            account.setBalance(accountRequest.getBalance());
            accountRepository.save(account);

            // Map the newly created Account entity to AccountResponse DTO
            accountResponse = mapToAccountDto(account);
            return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("An error occurred while creating the account with userId: {}", accountRequest.getUserId(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Maps an Account entity to an AccountResponse DTO.
     *
     * @param account The Account entity to map.
     * @return The AccountResponse DTO containing the mapped data.
     */
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
