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
     * This endpoint retrieves the data of a specific user based on their user ID.
     * Access to this endpoint is restricted based on the user's role.
     *
     * @param userId The ID of the user to be fetched.
     * @param jwt    The JWT token of the currently authenticated user, used to verify their role and permissions.
     * @return ResponseEntity containing the user data if the user has the required role;
     * otherwise, returns an unauthorized error.
     */
    @GetMapping("{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId, @AuthenticationPrincipal Jwt jwt) {
        if (RoleTools.hasAccess(jwt, new ArrayList<>(List.of(
                UserRoleEnum.USER.name()
        )))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return userService.getUser(userId);
    }

    /**
     * Fetch all users from the database.
     * <p>
     * This endpoint retrieves a list of all users. Access is restricted based on the user's role.
     *
     * @param jwt The JWT token of the currently authenticated user, used to verify their role and permissions.
     * @return ResponseEntity containing a list of all users if the user has the required role; otherwise, returns an unauthorized error.
     */
    @GetMapping()
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal Jwt jwt) {
        if (RoleTools.hasAccess(jwt, new ArrayList<>(List.of(
                UserRoleEnum.USER.name()
        )))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
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
     * This endpoint allows updating the details of a specific user based on their user ID.
     * The access to this endpoint is restricted and is allowed only to users with specific roles.
     *
     * @param userId      The ID of the user to be updated.
     * @param userRequest The new data for the user provided in the request body.
     * @param jwt         The JWT token of the currently authenticated user, used to verify their role and permissions.
     * @return ResponseEntity containing the updated user data if the user has the required role;
     *         otherwise, returns an unauthorized error.
     */
    @PutMapping("{userId}")
    public ResponseEntity<?> update(@PathVariable String userId, @RequestBody UserRequest userRequest, @AuthenticationPrincipal Jwt jwt) {
        if (!RoleTools.hasAccess(jwt, new ArrayList<>(List.of(
                UserRoleEnum.ADMIN.name(),
                UserRoleEnum.USER.name()
        )))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return userService.update(userId, userRequest);
    }

    /**
     * Activate a user by their unique ID.
     *
     * @param userId The ID of the user to be activated.
     * @return ResponseEntity containing the activated user data or an error message.
     */
    @PutMapping("{userId}/activate")
    public ResponseEntity<?> activate(@PathVariable String userId, @AuthenticationPrincipal Jwt jwt) {
        if (!RoleTools.hasAccess(jwt, new ArrayList<>(List.of(
                UserRoleEnum.ADMIN.name(),
                UserRoleEnum.USER.name()
        )))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return userService.activate(userId);
    }

    /**
     * Delete a user by their unique ID.
     *
     * @param userId The ID of the user to be deleted.
     * @return ResponseEntity containing a confirmation message or an error message.
     */
    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId, @AuthenticationPrincipal Jwt jwt) {
        if (!RoleTools.hasAccess(jwt, new ArrayList<>(List.of(
                UserRoleEnum.ADMIN.name(),
                UserRoleEnum.USER.name()
        )))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return userService.deleteUser(userId);
    }
}
