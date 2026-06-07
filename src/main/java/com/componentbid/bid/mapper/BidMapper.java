package com.componentbid.bid.mapper;

import com.componentbid.bid.dto.BidDto;
import com.componentbid.bid.entity.Bid;
import com.componentbid.user.mapper.UserMapper;

public class BidMapper {
    public static BidDto projectBid(Bid bid) {
        return BidDto.builder()
                .id(bid.getId())
                .bidder(UserMapper.projectUser(bid.getUser()))
                .sum(bid.getSum())
                .createdAt(bid.getCreatedAt())
                .build();
    }
}
