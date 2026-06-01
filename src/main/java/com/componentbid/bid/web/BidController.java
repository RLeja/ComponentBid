package com.componentbid.bid.web;

import com.componentbid.bid.dto.BidCreateRequest;
import com.componentbid.bid.entity.Bid;
import com.componentbid.bid.repository.BidRepository;
import com.componentbid.bid.service.BidService;
import com.componentbid.user.entity.CustomUserDetails;
import com.componentbid.user.entity.User;
import com.componentbid.user.repository.UserRepository;
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

import java.math.BigDecimal;
import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@Controller
@RequestMapping("/auctions/{auctionId}/bids")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;
    private final UserRepository userRepository;

    @PostMapping
    public String placeBid(
            @PathVariable UUID auctionId,
            @Valid @ModelAttribute BidCreateRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal
            CustomUserDetails currentUser) {

        if (bindingResult.hasErrors()) {
            return "redirect:/auctions/" + auctionId;
        }
//        User user = userRepository
//                .findByEmail("john@example.com")
//                .orElseThrow(); //temporary, replace with current user later

        bidService.placeBid(
                auctionId,
                currentUser.getUser().getId(),
                request.getSum()
        );

        return "redirect:/auctions/" + auctionId;
    }
}
