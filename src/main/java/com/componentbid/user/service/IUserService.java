package com.componentbid.user.service;

import com.componentbid.user.dto.RegisterRequest;
import com.componentbid.user.dto.UserProfileUpdateRequest;
import com.componentbid.user.entity.User;

import java.util.UUID;

public interface IUserService {
    User register(RegisterRequest request);
    User findByEmail(String email);
    User getById(UUID id);
    User updateProfile(UUID id, UserProfileUpdateRequest request);
}
