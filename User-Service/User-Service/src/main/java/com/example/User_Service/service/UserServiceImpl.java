package com.example.User_Service.service;

import com.example.User_Service.dto.UpdatePasswordRequest;
import com.example.User_Service.dto.UpdateUserDto;
import com.example.User_Service.dto.UpdateUserResponse;
import com.example.User_Service.dto.UserDto;
import com.example.User_Service.feign.CartServiceClient;
import com.example.User_Service.model.User;
import com.example.User_Service.repository.AddressBookRepository;
import com.example.User_Service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private CartServiceClient cartServiceClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String getJwtToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            if (request.getCookies() != null) {
                for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                    if ("access_token".equals(cookie.getName())) {
                        return "Bearer " + cookie.getValue();
                    }
                }
            }
            return request.getHeader("Authorization");
        }
        return null;
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole().name());
        dto.setProfileImage(user.getProfileImage());
        return dto;
    }

    @Override
    public UserDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }

    @Override
    public UpdateUserResponse updateUserProfile(Long userId, UpdateUserDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean loginRequired = false;

        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new RuntimeException("Username is already taken");
            }
            user.setUsername(dto.getUsername());
            loginRequired = true;
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email is already taken");
            }
            user.setEmail(dto.getEmail());
            loginRequired = true;
        }

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getProfileImage() != null) user.setProfileImage(dto.getProfileImage());

        if (loginRequired) {
            user.setRefreshToken(null);
            user.setRefreshTokenExpiry(null);
        }

        user = userRepository.save(user);
        return new UpdateUserResponse(mapToDto(user), loginRequired);
    }

    @Override
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUserAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete user's cart in Cart-Service
        try {
            String token = getJwtToken();
            if (token != null) {
                cartServiceClient.clearCart(token);
            }
        } catch (Exception e) {
            System.err.println("Failed to clear cart for user: " + userId + " - " + e.getMessage());
        }

        // Delete user's addresses
        addressBookRepository.deleteByUserId(userId);

        // Delete user from the database
        userRepository.delete(user);
    }
}
