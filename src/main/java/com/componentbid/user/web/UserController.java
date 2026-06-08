package com.componentbid.user.web;

import com.componentbid.user.dto.ChangePasswordRequest;
import com.componentbid.user.dto.UserProfileUpdateRequest;
import com.componentbid.user.entity.CustomUserDetails;
import com.componentbid.user.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/{id}")
    public String profile(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Model model) {

        var userProfile = userService.getProfile(id);

        model.addAttribute("profile", userProfile);

        model.addAttribute(
                "isOwnProfile",
                currentUser != null &&
                        Objects.equals(currentUser.getUser().getId(), id)
        );

        return "user/user-profile";
    }

    @GetMapping("/profile")
    public String myProfile(
            @AuthenticationPrincipal
            CustomUserDetails currentUser) {

        return "redirect:/users/" +
                currentUser.getUser().getId();
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Model model) {

        var user = userService.get(currentUser.getUser().getId());

        if (!model.containsAttribute("profileUpdateRequest")) {
            UserProfileUpdateRequest request = new UserProfileUpdateRequest();
            request.setName(user.getName());
            request.setEmail(user.getEmail());

            model.addAttribute("profileUpdateRequest", request);
        }

        return "user/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @ModelAttribute UserProfileUpdateRequest profileUpdateRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "errorMessage",
                    "Please fix the highlighted profile fields."
            );

            return "user/edit-profile";
        }

        try {
            userService.updateProfile(
                    currentUser.getUser().getId(),
                    profileUpdateRequest
            );
        } catch (IllegalArgumentException | IllegalStateException exception) {
            model.addAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "user/edit-profile";
        }

        currentUser.getUser().setName(profileUpdateRequest.getName());
        currentUser.getUser().setEmail(profileUpdateRequest.getEmail());

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Profile updated."
        );

        return "redirect:/users/" + currentUser.getUser().getId();
    }

    @PostMapping("/profile/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteAccount(@AuthenticationPrincipal CustomUserDetails currentUser, HttpServletRequest request) throws Exception {

        userService.delete(currentUser.getUser().getId());
        request.logout();

        return "redirect:/";
    }

    @GetMapping("/profile/change-password")
    @PreAuthorize("isAuthenticated()")
    public String changePasswordPage(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());

        return "user/change-password";
    }

    @PostMapping("/profile/change-password")
    @PreAuthorize("isAuthenticated()")
    public String changePassword(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @ModelAttribute ChangePasswordRequest changePasswordRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Please fix the highlighted fields.");
            return "user/change-password";
        }

        try {
            userService.changePassword(currentUser.getUser().getId(), changePasswordRequest);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/change-password";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Password changed.");

        return "redirect:/users/" + currentUser.getUser().getId();
    }

    @PostMapping("/{id}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public String banUser(
            @PathVariable UUID id,
            RedirectAttributes redirectAttributes) {

        userService.banUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User banned.");

        return "redirect:/users/" + id;
    }
}
