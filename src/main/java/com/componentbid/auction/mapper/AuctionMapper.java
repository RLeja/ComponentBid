package com.componentbid.auction.mapper;

import com.componentbid.auction.dto.AuctionDetailsDto;
import com.componentbid.auction.entity.Auction;
import com.componentbid.bid.mapper.BidMapper;
import com.componentbid.user.mapper.UserMapper;

public class AuctionMapper {
    public static AuctionDetailsDto projectDetails(Auction auction) {
        return AuctionDetailsDto.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .description(auction.getDescription())
                .categoryName(auction.getCategory().getCategoryName())
                .manufacturerName(auction.getManufacturer().getManufacturerName())
                .conditionName(auction.getCondition().getConditionName())
                .startPrice(auction.getStartPrice())
                .startDate(auction.getStartDate())
                .endDate(auction.getEndDate())
                .active(auction.isActive())
                .seller(UserMapper.projectUser(auction.getUser()))
                .imageUrls(
                    auction.getImages()
                            .stream()
                            .map(image -> "/files/" + image.getId())
                            .toList()
                )
                .bids(
                        auction.getBids()
                                .stream()
                                .map(BidMapper::projectBid)
                                .toList()
                )
                .build();
    }
}
