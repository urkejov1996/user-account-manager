package com.example.useraccountmanager.controller;

import com.example.useraccountmanager.dto.request.UserRequest;
import com.example.useraccountmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * Fetch a user by their unique ID.
     * @param userId The ID of the user to be fetched.
     * @return ResponseEntity containing the user data or an error message.
     */
    @GetMapping("{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }


    @GetMapping()
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Create a new user.
     * @param userRequest The data for the new user.
     * @param bindingResult Binding result for validation errors.
     * @return ResponseEntity containing the created user data or an error message.
     */
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid UserRequest userRequest, BindingResult bindingResult) {
        return userService.create(userRequest, bindingResult);
    }

    /**
     * Update an existing user by their unique ID.
     * @param userId The ID of the user to be updated.
     * @param userRequest The new data for the user.
     * @return ResponseEntity containing the updated user data or an error message.
     */
    @PutMapping("{userId}")
    public ResponseEntity<?> update(@PathVariable String userId, @RequestBody UserRequest userRequest) {
        return userService.update(userId, userRequest);
    }

    /**
     * Delete a user by their unique ID.
     * @param userId The ID of the user to be deleted.
     * @return ResponseEntity containing a confirmation message or an error message.
     */
    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }
}
