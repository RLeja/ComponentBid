package com.componentbid.auction.web;

import com.componentbid.auction.dto.AuctionCreateRequest;
import com.componentbid.auction.dto.AuctionUpdateRequest;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {

        model.addAttribute("auction", auctionService.getAuctionEditForm(id));
        model.addAttribute("auctionId", id);
        populateCreateFormOptions(model);

        return "auction/edit";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable UUID id, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {

        var auctionDetails = auctionService.getAuctionDetails(id);

        model.addAttribute("auction", auctionDetails);
        populateCreateFormOptions(model);

        model.addAttribute("bidCreateRequest", new BidCreateRequest());
        model.addAttribute("reviewCreateRequest", new ReviewCreateRequest());

        if (currentUser != null) {
            var canReview = reviewService.canReview(id, currentUser.getUser().getId());
            model.addAttribute("canReview", canReview);

            boolean isAdmin = currentUser.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            var canEdit = isAdmin || auctionDetails.getSeller().getId().equals(currentUser.getUser().getId());
            model.addAttribute("canEdit", canEdit);
        }

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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/edit")
    public String editAuction(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @ModelAttribute("auction") AuctionUpdateRequest request,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("auctionId", id);
            populateCreateFormOptions(model);

            return "auction/edit";
        }

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        try {
            auctionService.updateAuction(id, request, currentUser.getUser().getId(), isAdmin);

            return "redirect:/auctions/" + id;

        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("auctionId", id);
            populateCreateFormOptions(model);

            return "auction/edit";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String deleteAuction(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails currentUser) {

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        auctionService.deleteAuction(id, currentUser.getUser().getId(), isAdmin);

        return "redirect:/";
    }

    private void populateCreateFormOptions(Model model) {
        model.addAttribute("categories", auctionService.getCategories());
        model.addAttribute("manufacturers", auctionService.getManufacturers());
        model.addAttribute("conditions", auctionService.getConditions());
    }
}
