package com.example.User_Service.service;

import com.example.User_Service.dto.UpdatePasswordRequest;
import com.example.User_Service.dto.UpdateUserDto;
import com.example.User_Service.dto.UpdateUserResponse;
import com.example.User_Service.dto.UserDto;

public interface UserService {
    UserDto getUserProfile(Long userId);
    UpdateUserResponse updateUserProfile(Long userId, UpdateUserDto dto);
    void updatePassword(Long userId, UpdatePasswordRequest request);
    void deleteUserAccount(Long userId);
}
