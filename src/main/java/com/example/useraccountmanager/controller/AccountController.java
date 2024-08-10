package com.example.useraccountmanager.controller;

import com.example.useraccountmanager.dto.request.AccountRequest;
import com.example.useraccountmanager.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("{userId}")
    public ResponseEntity<?> getAccount(@PathVariable String userId) {
        return accountService.getAccount(userId);
    }

    @PostMapping()
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest accountRequest, BindingResult bindingResult) {
        return accountService.createAccount(accountRequest, bindingResult);
    }
}
