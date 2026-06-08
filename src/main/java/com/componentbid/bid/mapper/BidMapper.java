package com.componentbid.bid.mapper;

import com.componentbid.auction.mapper.AuctionMapper;
import com.componentbid.bid.dto.BidDto;
import com.componentbid.bid.dto.BidListItemDto;
import com.componentbid.bid.entity.Bid;
import com.componentbid.user.mapper.UserMapper;

public class BidMapper {
    public static BidDto project(Bid bid) {
        return BidDto.builder()
                .id(bid.getId())
                .bidder(UserMapper.projectUser(bid.getUser()))
                .sum(bid.getSum())
                .createdAt(bid.getCreatedAt())
                .build();
    }

    public static BidListItemDto projectListItem(Bid bid) {
        return BidListItemDto.builder()
                .id(bid.getId())
                .sum(bid.getSum())
                .createdAt(bid.getCreatedAt())
                .auction(AuctionMapper.projectPreview(bid.getAuction()))
                .build();
    }
}
