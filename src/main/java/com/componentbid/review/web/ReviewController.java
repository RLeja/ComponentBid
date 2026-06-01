package com.componentbid.review.web;

import com.componentbid.review.dto.ReviewCreateRequest;
import com.componentbid.review.service.ReviewService;
import com.componentbid.user.entity.CustomUserDetails;
import com.componentbid.user.entity.User;
import com.componentbid.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@Controller
@RequestMapping("/auctions/{auctionId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    @PostMapping
    public String createReview(
            @PathVariable UUID auctionId,
            @Valid @ModelAttribute ReviewCreateRequest request,
            @AuthenticationPrincipal
            CustomUserDetails currentUser) {

//        User currentUser =
//                userRepository
//                        .findByEmail(
//                                "john@example.com"
//                        )
//                        .orElseThrow();

        reviewService.createReview(
                auctionId,
                currentUser.getUser().getId(),
                request
        );

        return "redirect:/auctions/" + auctionId;
    }

}
