package com.example.useraccountmanager.controller;

import com.example.useraccountmanager.dto.request.UserRequest;
import com.example.useraccountmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody UserRequest userRequest, BindingResult bindingResult) {
       return userService.create(userRequest, bindingResult);
    }
}
