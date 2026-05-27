package com.componentbid.user.service;

import com.componentbid.user.dto.CreateUserRequest;
import com.componentbid.user.entity.User;

public interface IUserService {
    User createUser(CreateUserRequest request);
    User findByEmail(String email);
}
