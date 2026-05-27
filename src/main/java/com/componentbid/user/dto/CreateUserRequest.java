package com.componentbid.user.dto;

import com.componentbid.user.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserRequest {
    private String email;
    private String name;
    private String passwordHash;
    private List<Role> roles;
}
