package com.componentbid.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private String name;
}
