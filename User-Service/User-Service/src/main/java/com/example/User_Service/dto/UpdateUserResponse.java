package com.example.User_Service.dto;

public class UpdateUserResponse {
    private UserDto user;
    private boolean requiresRelogin;

    public UpdateUserResponse() {}

    public UpdateUserResponse(UserDto user, boolean requiresRelogin) {
        this.user = user;
        this.requiresRelogin = requiresRelogin;
    }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }
    public boolean isRequiresRelogin() { return requiresRelogin; }
    public void setRequiresRelogin(boolean requiresRelogin) { this.requiresRelogin = requiresRelogin; }
}
