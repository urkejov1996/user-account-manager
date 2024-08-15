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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users. This class provides methods to perform CRUD operations on the User entity.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return ResponseEntity containing the UserResponse if found, or an error message if not.
     */
    public ResponseEntity<?> getUser(String userId) {
        UserResponse userResponse = new UserResponse();
        try {
            // Validate userId input
            if (userId.isEmpty() || userId.isBlank()) {
                userResponse.addError(ErrorMessage.BAD_REQUEST);
                return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
            }

            // Fetch user from the repository
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                userResponse.addError(ErrorMessage.NOT_FOUND);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Map User entity to UserResponse DTO
            User user = optionalUser.get();
            userResponse = mapToUserDto(user);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while retrieving the user with ID: {}", userId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return ResponseEntity containing a list of UserResponse DTOs for all users, or an information message if no users are found.
     */
    public ResponseEntity<?> getAllUsers() {
        UserResponse userResponse = new UserResponse();
        try {
            // Fetch all users from the repository
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                userResponse.addInfo("There are no users in the database.");
                userResponse.setData(new ArrayList<>());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // Map each User entity to a UserResponse DTO
            List<UserResponse> userResponses = users.stream()
                    .map(this::mapToUserDto)
                    .toList();
            userResponse.setData(userResponses);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while retrieving all users", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new user.
     *
     * @param userRequest   The UserRequest DTO containing the new user's data.
     * @param bindingResult The BindingResult object that holds the result of the validation and binding and contains errors that may have occurred.
     * @return ResponseEntity containing the created UserResponse or error message if the creation fails.
     */
    public ResponseEntity<?> create(UserRequest userRequest, BindingResult bindingResult) {
        UserResponse userResponse = new UserResponse();

        // Validate the input request
        if (bindingResult.hasErrors()) {
            UserResponse finalUserResponse = userResponse;
            bindingResult.getAllErrors().forEach(error -> {
                finalUserResponse.addError(error.getDefaultMessage());
            });
            return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            // Check if a user with the same email already exists
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                userResponse.addError(ErrorMessage.ALREADY_EXIST);
                return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
            }

            // Create new User entity from UserRequest DTO
            User user = new User();
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPhoneNumber(userRequest.getPhoneNumber());
            user.setUserStatus(userRequest.getStatus());
            user.setAddress(userRequest.getAddress());

            // Map AccountRequests to Account entities and associate them with the user
            if (userRequest.getAccountRequests() != null) {
                Set<Account> accounts = userRequest.getAccountRequests().stream().map(accountRequest -> {
                    Account account = new Account();
                    account.setBalance(accountRequest.getBalance());
                    account.setUser(user);
                    return account;
                }).collect(Collectors.toSet());
                user.setAccounts(accounts);
            }

            // Save the new user to the repository
            userRepository.save(user);
            userResponse = mapToUserDto(user);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("An error occurred while creating user with email {}", userRequest.getEmail(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing user by their ID.
     *
     * @param userId      The ID of the user to update.
     * @param userRequest The UserRequest DTO containing the updated data for the user.
     * @return ResponseEntity containing the updated UserResponse or error message if the update fails.
     */
    public ResponseEntity<?> update(String userId, UserRequest userRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            // Validate userId input
            if (userId == null || userId.isEmpty()) {
                userResponse.addError(ErrorMessage.BAD_REQUEST);
                return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
            }

            // Fetch existing user from the repository
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                userResponse.addError(ErrorMessage.NOT_FOUND);
                return new ResponseEntity<>(userResponse, HttpStatus.NOT_FOUND);
            }

            // Update the user fields with the data from UserRequest
            User existingUser = optionalUser.get();
            updateUserFields(existingUser, userRequest);
            userRepository.save(existingUser);
            userResponse = mapToUserDto(existingUser);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while updating user with ID: {}", userId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     * @return ResponseEntity with an information message if the deletion is successful or an error message if it fails.
     */
    public ResponseEntity<?> deleteUser(String userId) {
        try {

            // Validate userId input
            if (userId == null || userId.isEmpty()) {
                return new ResponseEntity<>(ErrorMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST);
            }

            // Fetch existing user from the repository
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return new ResponseEntity<>(ErrorMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            // Delete the user from the repository
            userRepository.deleteById(userId);
            return new ResponseEntity<>(InfoMessage.DELETED, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while deleting user with ID: {}", userId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Maps a User entity to a UserResponse DTO.
     *
     * @param user The User entity to map.
     * @return The UserResponse DTO containing the mapped data.
     */
    private UserResponse mapToUserDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .status(user.getUserStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .accountIds(user.getAccounts().stream().map(account -> account.getId()).collect(Collectors.toSet()))
                .build();
    }

    /**
     * Updates the fields of an existing user entity with the data from a UserRequest DTO.
     *
     * @param existingUser The existing User entity to update.
     * @param userRequest  The UserRequest DTO containing the updated data.
     */
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
        if (userRequest.getStatus() != null && !userRequest.getStatus().equals(existingUser.getUserStatus())) {
            existingUser.setUserStatus(userRequest.getStatus());
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
