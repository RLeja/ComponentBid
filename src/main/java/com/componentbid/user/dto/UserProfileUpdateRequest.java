package com.componentbid.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequest {
    @NotBlank(message = "Name is required.")
    @Size(max = 100, message = "Name must be 100 characters or less.")
    private String name;

    @Email(message = "Enter a valid email address.")
    @NotBlank(message = "Email is required.")
    private String email;
}
