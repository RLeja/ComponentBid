package com.componentbid.auth.service;

import com.componentbid.auth.dto.RegisterRequest;

public interface IAuthService {
    void Register(RegisterRequest request);
    void Login(RegisterRequest request);
}
