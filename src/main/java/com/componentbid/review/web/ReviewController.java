package com.componentbid.review.web;

import com.componentbid.review.dto.ReviewCreateRequest;
import com.componentbid.review.service.ReviewService;
import com.componentbid.user.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@Controller
@RequestMapping("/auctions/{auctionId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public String createReview(
            @PathVariable UUID auctionId,
            @Valid @ModelAttribute("reviewCreateRequest") ReviewCreateRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal
            CustomUserDetails currentUser,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.reviewCreateRequest",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute(
                    "reviewCreateRequest",
                    request
            );
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Please fix the review fields."
            );

            return "redirect:/auctions/" + auctionId;
        }

        try {
            reviewService.createReview(
                    auctionId,
                    currentUser.getUser().getId(),
                    request
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Review submitted successfully."
            );
        } catch (IllegalArgumentException | IllegalStateException exception) {
            redirectAttributes.addFlashAttribute(
                    "reviewCreateRequest",
                    request
            );
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/auctions/" + auctionId;
    }

}
