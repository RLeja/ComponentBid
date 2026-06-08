package com.componentbid.user.service;

import com.componentbid.user.dto.RegisterRequest;
import com.componentbid.user.dto.UserDto;
import com.componentbid.user.dto.UserProfileDto;
import com.componentbid.user.dto.UserProfileUpdateRequest;
import com.componentbid.user.entity.User;

import java.util.UUID;

public interface IUserService {
    UserProfileDto getProfile(UUID id);
    UserDto get(UUID id);
    User register(RegisterRequest request);
    void updateProfile(UUID id, UserProfileUpdateRequest request);
}
