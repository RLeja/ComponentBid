package com.componentbid.user.mapper;

import com.componentbid.user.dto.UserDto;
import com.componentbid.user.entity.User;
import org.springframework.stereotype.Component;

public class UserMapper {
    public static UserDto projectUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
