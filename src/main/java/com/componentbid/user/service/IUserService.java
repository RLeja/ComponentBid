package com.componentbid.user.service;

import com.componentbid.user.dto.*;
import com.componentbid.user.entity.User;

import java.util.UUID;

public interface IUserService {
    UserProfileDto getProfile(UUID id);
    UserDto get(UUID id);
    User register(RegisterRequest request);
    void updateProfile(UUID id, UserProfileUpdateRequest request);
    void delete(UUID id);
    void changePassword(UUID id, ChangePasswordRequest request);
    void banUser(UUID id);
}
