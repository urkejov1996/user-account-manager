package com.example.useraccountmanager.service;

import com.example.useraccountmanager.dto.request.UserRequest;
import com.example.useraccountmanager.dto.response.UserResponse;
import com.example.useraccountmanager.model.Account;
import com.example.useraccountmanager.model.User;
import com.example.useraccountmanager.repository.UserRepository;
import com.example.useraccountmanager.tools.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<?> getUser(String userId) {
        UserResponse userResponse = new UserResponse();
        try {
            if (userId.isEmpty() || userId.isBlank()) {
                userResponse.addError(ErrorMessage.BAD_REQUEST);
                return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
            }
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                userResponse.addError(ErrorMessage.NOT_FOUND);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            User user = optionalUser.get();
            userResponse = mapToDto(user);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while retrieving the user with ID: {}", userId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> create(UserRequest userRequest, BindingResult bindingResult) {
        UserResponse userResponse = new UserResponse();
        if (bindingResult.hasErrors()) {
            UserResponse finalUserResponse = userResponse;
            bindingResult.getAllErrors().forEach(error -> {
                finalUserResponse.addError(error.getDefaultMessage());
            });
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                userResponse.addError(ErrorMessage.ALREADY_EXIST);
                return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
            }
            User user = new User();
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPhoneNumber(userRequest.getPhoneNumber());
            user.setAddress(userRequest.getAddress());
            user.setLastLoginDate(userRequest.getLastLoginDate());
            Set<Account> accounts = userRequest.getAccountRequests().stream().map(accountRequest -> {
                Account account = new Account();
                account.setBalance(accountRequest.getBalance());
                account.setUser(user);
                return account;
            }).collect(Collectors.toSet());
            user.setAccounts(accounts);
            userRepository.save(user);
            userResponse = mapToDto(user);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("An error occurred while creating user with email {}", userRequest.getEmail(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserResponse mapToDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .lastLoginDate(user.getLastLoginDate())
                .accountIds(user.getAccounts().stream().map(account -> account.getId()).collect(Collectors.toSet()))
                .build();
    }

}
