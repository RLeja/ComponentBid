package com.componentbid.bid.web;

import com.componentbid.bid.dto.BidCreateRequest;
import com.componentbid.bid.service.BidService;
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
@RequestMapping("/auctions/{auctionId}/bids")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping
    public String placeBid(
            @PathVariable UUID auctionId,
            @Valid @ModelAttribute("bidCreateRequest") BidCreateRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal
            CustomUserDetails currentUser,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.bidCreateRequest",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute(
                    "bidCreateRequest",
                    request
            );
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Please fix the bid amount."
            );

            return "redirect:/auctions/" + auctionId;
        }

        try {
            bidService.placeBid(
                    auctionId,
                    currentUser.getUser().getId(),
                    request.getSum()
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Bid placed successfully."
            );
        } catch (IllegalArgumentException | IllegalStateException exception) {
            redirectAttributes.addFlashAttribute(
                    "bidCreateRequest",
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
