package com.componentbid.auction.web;

import com.componentbid.auction.dto.AuctionCreateRequest;
import com.componentbid.auction.service.IAuctionService;
import com.componentbid.bid.dto.BidCreateRequest;
import com.componentbid.review.dto.ReviewCreateRequest;
import com.componentbid.review.service.IReviewService;
import com.componentbid.user.entity.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final IAuctionService auctionService;
    private final IReviewService reviewService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute("auction", new AuctionCreateRequest());
        populateCreateFormOptions(model);

        return "auction/create";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable UUID id, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {

        var auctionDetails = auctionService.getAuctionDetails(id);

        model.addAttribute("auction", auctionDetails);

        model.addAttribute("bidCreateRequest", new BidCreateRequest());
        model.addAttribute("reviewCreateRequest", new ReviewCreateRequest());

        var canReview = currentUser != null && reviewService.canReview(id, currentUser.getUser().getId());
        model.addAttribute("canReview", canReview);

        return "auction/details";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createAuction(
            @Valid @ModelAttribute("auction") AuctionCreateRequest request,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (bindingResult.hasErrors()) {
            populateCreateFormOptions(model);
            model.addAttribute(
                    "errorMessage",
                    "Please fix the highlighted auction fields."
            );

            return "auction/create";
        }

        try {
            UUID auctionId = auctionService.createAuction(
                    request,
                    currentUser.getUser().getId()
            );

            return "redirect:/auctions/" + auctionId;

        } catch (IllegalArgumentException | IllegalStateException exception) {
            populateCreateFormOptions(model);
            model.addAttribute("errorMessage", exception.getMessage());

            return "auction/create";
        }
    }

    private void populateCreateFormOptions(Model model) {
        model.addAttribute("categories", auctionService.getCategories());
        model.addAttribute("manufacturers", auctionService.getManufacturers());
        model.addAttribute("conditions", auctionService.getConditions());
    }
}
