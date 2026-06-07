package com.componentbid.user.web;

import com.componentbid.user.dto.UserProfileUpdateRequest;
import com.componentbid.user.entity.CustomUserDetails;
import com.componentbid.user.entity.User;
import com.componentbid.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final UserService userService;

    @GetMapping("/{id}")
    public String profile(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Model model) {

        User user = userService.getById(id);

        model.addAttribute(
                "user",
                user
        );

        model.addAttribute(
                "auctions",
                user.getAuctions()
        );

        model.addAttribute(
                "bids",
                user.getBids()
        );

        model.addAttribute(
                "writtenReviews",
                user.getWrittenReviews()
        );

        model.addAttribute(
                "receivedReviews",
                user.getReceivedReviews()
        );

        model.addAttribute(
                "isOwnProfile",
                currentUser != null &&
                        Objects.equals(currentUser.getUser().getId(), id)
        );

        return "user-profile";
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

        User user = userService.getById(currentUser.getUser().getId());

        if (!model.containsAttribute("profileUpdateRequest")) {
            UserProfileUpdateRequest request = new UserProfileUpdateRequest();
            request.setName(user.getName());
            request.setEmail(user.getEmail());

            model.addAttribute("profileUpdateRequest", request);
        }

        return "edit-profile";
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

            return "edit-profile";
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

            return "edit-profile";
        }

        currentUser.getUser().setName(profileUpdateRequest.getName());
        currentUser.getUser().setEmail(profileUpdateRequest.getEmail());

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Profile updated."
        );

        return "redirect:/users/" + currentUser.getUser().getId();
    }
}
