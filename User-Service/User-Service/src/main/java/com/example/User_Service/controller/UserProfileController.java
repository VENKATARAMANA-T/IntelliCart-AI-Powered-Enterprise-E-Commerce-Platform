package com.example.User_Service.controller;

import com.example.User_Service.dto.UpdatePasswordRequest;
import com.example.User_Service.dto.UpdateUserDto;
import com.example.User_Service.dto.UpdateUserResponse;
import com.example.User_Service.dto.UserDto;
import com.example.User_Service.security.AuthenticatedUser;
import com.example.User_Service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/profile")
@PreAuthorize("isAuthenticated()")
public class UserProfileController {

    @Autowired
    private UserService userService;

    private Long getUserIdFromAuth(Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return user.getUserId();
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserProfile(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        UserDto profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<UpdateUserResponse> updateUserProfile(
            @RequestBody UpdateUserDto dto, 
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        UpdateUserResponse response = userService.updateUserProfile(userId, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody UpdatePasswordRequest request,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        userService.updatePassword(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserAccount(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        userService.deleteUserAccount(userId);
        return ResponseEntity.noContent().build();
    }
}
