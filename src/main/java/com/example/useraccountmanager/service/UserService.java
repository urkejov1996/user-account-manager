package com.example.useraccountmanager.service;

import com.example.useraccountmanager.dto.request.AccountRequest;
import com.example.useraccountmanager.dto.request.UserRequest;
import com.example.useraccountmanager.dto.response.UserResponse;
import com.example.useraccountmanager.model.Account;
import com.example.useraccountmanager.model.User;
import com.example.useraccountmanager.repository.UserRepository;
import com.example.useraccountmanager.tools.ErrorMessage;
import com.example.useraccountmanager.tools.InfoMessage;
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

    public ResponseEntity<?> update(String userId, UserRequest userRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            if (userId == null || userId.isEmpty()) {
                userResponse.addError(ErrorMessage.BAD_REQUEST);
                return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
            }
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                userResponse.addError(ErrorMessage.NOT_FOUND);
                return new ResponseEntity<>(userResponse, HttpStatus.NOT_FOUND);
            }

            User existingUser = optionalUser.get();
            updateUserFields(existingUser, userRequest);
            userRepository.save(existingUser);
            userResponse = mapToDto(existingUser);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while updating user with ID: {}", userId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteUser(String userId) {
        try {
            if (userId == null || userId.isEmpty()) {
                return new ResponseEntity<>(ErrorMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST);
            }

            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return new ResponseEntity<>(ErrorMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            userRepository.deleteById(userId);
            return new ResponseEntity<>(InfoMessage.DELETED, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while deleting user with ID: {}", userId, e);
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

    private void updateUserFields(User existingUser, UserRequest userRequest) {
        if (userRequest.getFirstName() != null && !userRequest.getFirstName().equals(existingUser.getFirstName())) {
            existingUser.setFirstName(userRequest.getFirstName());
        }
        if (userRequest.getLastName() != null && !userRequest.getLastName().equals(existingUser.getLastName())) {
            existingUser.setLastName(userRequest.getLastName());
        }
        if (userRequest.getUsername() != null && !userRequest.getUsername().equals(existingUser.getUsername())) {
            existingUser.setUsername(userRequest.getUsername());
        }
        if (userRequest.getEmail() != null && !userRequest.getEmail().equals(existingUser.getEmail())) {
            existingUser.setEmail(userRequest.getEmail());
        }
        if (userRequest.getPhoneNumber() != null && !userRequest.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
            existingUser.setPhoneNumber(userRequest.getPhoneNumber());
        }
        if (userRequest.getAddress() != null && !userRequest.getAddress().equals(existingUser.getAddress())) {
            existingUser.setAddress(userRequest.getAddress());
        }
        if (userRequest.getAccountRequests() != null) {
            Set<Account> existingAccounts = existingUser.getAccounts();
            existingAccounts.clear();
            for (AccountRequest accountRequest : userRequest.getAccountRequests()) {
                Account account = new Account();
                account.setBalance(accountRequest.getBalance());
                account.setUser(existingUser);
                existingAccounts.add(account);
            }
        }


    }


}
