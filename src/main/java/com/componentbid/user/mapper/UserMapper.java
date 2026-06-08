package com.componentbid.user.mapper;

import com.componentbid.auction.mapper.AuctionMapper;
import com.componentbid.bid.mapper.BidMapper;
import com.componentbid.review.mapper.ReviewMapper;
import com.componentbid.user.dto.UserDto;
import com.componentbid.user.dto.UserProfileDto;
import com.componentbid.user.entity.User;
import org.springframework.stereotype.Component;

public class UserMapper {
    public static UserDto projectUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserProfileDto projectProfile(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .averageRating(user.getAverageRating())
                .auctions(
                        user.getAuctions().stream()
                            .map(AuctionMapper::projectListItem)
                            .toList()
                )
                .bids(
                        user.getBids().stream()
                            .map(BidMapper::projectListItem)
                            .toList()
                )
                .writtenReviews(
                        user.getWrittenReviews().stream()
                            .map(ReviewMapper::project)
                            .toList()
                )
                .receivedReviews(
                        user.getReceivedReviews().stream()
                            .map(ReviewMapper::project)
                            .toList())
                .build();
    }
}
