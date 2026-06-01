package com.componentbid.user.web;

import com.componentbid.user.entity.CustomUserDetails;
import com.componentbid.user.entity.User;
import com.componentbid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public String profile(
            @PathVariable UUID id,
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

        return "user-profile";
    }
    @GetMapping("/profile")
    public String myProfile(
            @AuthenticationPrincipal
            CustomUserDetails currentUser) {

        return "redirect:/users/" +
                currentUser.getUser().getId();
    }
}