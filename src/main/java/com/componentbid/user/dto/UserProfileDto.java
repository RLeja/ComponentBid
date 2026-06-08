package com.componentbid.user.dto;

import com.componentbid.auction.dto.AuctionListItemDto;
import com.componentbid.bid.dto.BidListItemDto;
import com.componentbid.review.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileDto {
    private UUID id;
    private String name;
    private String email;
    private Double averageRating;
    private Collection<AuctionListItemDto> auctions;
    private Collection<BidListItemDto> bids;
    private Collection<ReviewDto> writtenReviews;
    private Collection<ReviewDto> receivedReviews;
}