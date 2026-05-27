package com.componentbid.user.service;

import com.componentbid.user.repository.UserRepository;
import com.componentbid.user.dto.CreateUserRequest;
import com.componentbid.user.entity.User;
import com.componentbid.user.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(CreateUserRequest request) {
        var user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPasswordHash(request.getPasswordHash());
        user.setRoles(request.getRoles());

        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }
}
