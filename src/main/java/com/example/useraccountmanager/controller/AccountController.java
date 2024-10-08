package com.example.useraccountmanager.controller;

import com.example.useraccountmanager.dto.request.AccountRequest;
import com.example.useraccountmanager.service.AccountService;
import com.example.useraccountmanager.tools.RoleTools;
import com.example.useraccountmanager.tools.enums.UserRoleEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Retrieves the account associated with the provided user ID.
     *
     * @param userId The ID of the user whose account is being retrieved.
     * @param accountId The ID of the account being retrieved.
     * @return ResponseEntity containing the account data or an error message if not found.
     */
    @GetMapping("{userId}/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable String userId, @PathVariable String accountId, @AuthenticationPrincipal Jwt jwt) {
        if (!RoleTools.hasAccess(jwt, new ArrayList<>(List.of(
                UserRoleEnum.ADMIN.name()
        )))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return accountService.getAccount(userId, accountId);
    }

    /**
     * Retrieves all accounts associated with the provided user ID.
     *
     * @param userId The ID of the user whose accounts are being retrieved.
     * @return ResponseEntity containing the list of accounts or an informational message if no accounts exist.
     */
    @GetMapping("{userId}")
    public ResponseEntity<?> getAllAccounts(@PathVariable String userId, @AuthenticationPrincipal Jwt jwt) {
        if (!RoleTools.hasAccess(jwt, new ArrayList<>(List.of(
                UserRoleEnum.ADMIN.name()
        )))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return accountService.getAllAccounts(userId);
    }

    /**
     * Creates a new account for the user based on the provided account request data.
     *
     * @param accountRequest DTO containing data for creating the account
     * @param bindingResult  Used to capture validation errors during request processing
     * @return ResponseEntity containing the created account data or validation errors
     */
    @PostMapping()
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountRequest accountRequest, BindingResult bindingResult) {
        return accountService.createAccount(accountRequest, bindingResult);
    }

    /**
     * Updates the balance of an existing account.
     *
     * @param accountRequest DTO containing the updated account balance information.
     * @return ResponseEntity containing the updated account data or an error message if the update fails.
     */
    @PutMapping("{userId}/{accountId}")
    public ResponseEntity<?> updateAccountBalance(@RequestBody AccountRequest accountRequest,@PathVariable String accountId){
        return accountService.updateAccountBalance(accountRequest,accountId);
    }
}
