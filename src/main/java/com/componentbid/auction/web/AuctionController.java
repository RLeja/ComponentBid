package com.componentbid.auction.web;

import com.componentbid.auction.dto.AuctionCreateRequest;
import com.componentbid.auction.repository.CategoryRepository;
import com.componentbid.auction.repository.ItemConditionRepository;
import com.componentbid.auction.repository.ManufacturerRepository;
import com.componentbid.auction.service.AuctionService;
import com.componentbid.bid.dto.BidCreateRequest;
import com.componentbid.bid.repository.BidRepository;
import com.componentbid.review.dto.ReviewCreateRequest;
import com.componentbid.review.service.ReviewService;
import com.componentbid.user.entity.CustomUserDetails;
import com.componentbid.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;
    private final BidRepository bidRepository;

    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ItemConditionRepository conditionRepository;
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute("auction", new AuctionCreateRequest());

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("manufacturers", manufacturerRepository.findAll());
        model.addAttribute("conditions", conditionRepository.findAll());

        return "create-auction";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable UUID id, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {

        var auctionDetails = auctionService.getAuctionDetails(id);

        model.addAttribute("auction", auctionDetails);

        model.addAttribute(
                "bids",
                bidRepository.findByAuction_IdOrderBySumDesc(id) //TODO: map in service
        );

        if (!model.containsAttribute("bidCreateRequest")) {
            model.addAttribute(
                    "bidCreateRequest",
                    new BidCreateRequest()
            );
        }

        if (!model.containsAttribute("reviewCreateRequest")) {
            model.addAttribute(
                    "reviewCreateRequest",
                    new ReviewCreateRequest()
            );
        }

        var canReview = currentUser != null && reviewService.canReview(id, currentUser.getUser().getId());
        model.addAttribute("canReview", canReview);

        return "auction-details";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createAuction(
            @Valid @ModelAttribute("auction") AuctionCreateRequest request,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal
            CustomUserDetails currentUser) {

        if (bindingResult.hasErrors()) {
            populateCreateFormOptions(model);
            model.addAttribute(
                    "errorMessage",
                    "Please fix the highlighted auction fields."
            );

            return "create-auction";
        }

        try {
            UUID auctionId = auctionService.createAuction(
                    request,
                    currentUser.getUser().getId()
            );

            return "redirect:/auctions/" + auctionId;
        } catch (IllegalArgumentException | IllegalStateException exception) {
            populateCreateFormOptions(model);
            model.addAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            return "create-auction";
        }
    }

    private void populateCreateFormOptions(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("manufacturers", manufacturerRepository.findAll());
        model.addAttribute("conditions", conditionRepository.findAll());
    }
}
