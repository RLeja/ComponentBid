package com.componentbid.user.service;

import com.componentbid.review.repository.ReviewRepository;
import com.componentbid.user.dto.UserDto;
import com.componentbid.user.dto.UserProfileDto;
import com.componentbid.user.dto.UserProfileUpdateRequest;
import com.componentbid.user.entity.Role;
import com.componentbid.user.entity.UserRole;
import com.componentbid.user.mapper.UserMapper;
import com.componentbid.user.repository.RoleRepository;
import com.componentbid.user.repository.UserRepository;
import com.componentbid.user.dto.RegisterRequest;
import com.componentbid.user.entity.User;
import com.componentbid.user.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException(
                    "Email already exists"
            );
        }

        var user = new User();

        user.setEmail(request.getEmail());
        user.setName(request.getName());

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository
                .findByName(UserRole.USER)
                .orElseThrow();
        user.setRoles(List.of(userRole));

        user.setAverageRating(0.0);

        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    @Override
    public UserDto get(UUID id) {
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.projectUser(user);
    }

    @Override
    public UserProfileDto getProfile(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.projectProfile(user);
    }

    @Override
    public void updateProfile(UUID id, UserProfileUpdateRequest request) {
        User user = userRepository.getReferenceById(id);

        userRepository.findByEmail(request.getEmail())
                .filter(existingUser -> !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    throw new IllegalStateException("Email already exists");
                });

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        userRepository.save(user);
    }
}
