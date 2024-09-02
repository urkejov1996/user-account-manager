package com.example.useraccountmanager.controller;

import com.example.useraccountmanager.dto.request.UserRequest;
import com.example.useraccountmanager.service.UserService;
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
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * Fetch a user by their unique ID.
     *
     * @param userId The ID of the user to be fetched.
     * @return ResponseEntity containing the user data or an error message.
     */
    @GetMapping("{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    /**
     * Fetch all users from the database.
     *
     * @return ResponseEntity containing a list of all users or an error message if no users are found.
     */
    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Creates a new user in the system.
     *
     * @param userRequest   The DTO containing the data for the new user to be created.
     * @param jwt           The JWT token of the currently authenticated user, used to check their role and permissions.
     * @param bindingResult The BindingResult object to capture any validation errors for the incoming user request.
     * @return ResponseEntity containing the created user's data if the user has sufficient permissions and there are no validation errors;
     * otherwise, returns an appropriate error response.
     */
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid UserRequest userRequest, @AuthenticationPrincipal Jwt jwt, BindingResult bindingResult) {
        if (!RoleTools.hasAccess(jwt, new ArrayList<>(List.of(
                UserRoleEnum.ADMIN.name(),
                UserRoleEnum.USER.name()
        )))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return userService.create(userRequest, bindingResult);
    }

    /**
     * Update an existing user by their unique ID.
     *
     * @param userId      The ID of the user to be updated.
     * @param userRequest The new data for the user.
     * @return ResponseEntity containing the updated user data or an error message.
     */
    @PutMapping("{userId}")
    public ResponseEntity<?> update(@PathVariable String userId, @RequestBody UserRequest userRequest) {
        return userService.update(userId, userRequest);
    }

    /**
     * Activate a user by their unique ID.
     *
     * @param userId The ID of the user to be activated.
     * @return ResponseEntity containing the activated user data or an error message.
     */
    @PutMapping("{userId}/activate")
    public ResponseEntity<?> activate(@PathVariable String userId) {
        return userService.activate(userId);
    }

    /**
     * Delete a user by their unique ID.
     *
     * @param userId The ID of the user to be deleted.
     * @return ResponseEntity containing a confirmation message or an error message.
     */
    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }
}
