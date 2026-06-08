package com.componentbid.auction.mapper;

import com.componentbid.auction.dto.AuctionCreateRequest;
import com.componentbid.auction.dto.AuctionDetailsDto;
import com.componentbid.auction.dto.AuctionListItemDto;
import com.componentbid.auction.entity.Auction;
import com.componentbid.auction.entity.Category;
import com.componentbid.auction.entity.ItemCondition;
import com.componentbid.auction.entity.Manufacturer;
import com.componentbid.bid.mapper.BidMapper;
import com.componentbid.user.entity.User;
import com.componentbid.user.mapper.UserMapper;

import java.time.LocalDateTime;

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

    public static AuctionListItemDto projectListItem(Auction auction) {
        return AuctionListItemDto.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .description(auction.getDescription())
                .categoryName(auction.getCategory().getCategoryName())
                .conditionName(auction.getCondition().getConditionName())
                .manufacturerName(auction.getManufacturer().getManufacturerName())
                .startPrice(auction.getStartPrice())
                .imageUrl(auction.getImages().isEmpty()
                        ? null
                        : "/files/" + auction.getImages().iterator().next().getId())
                .build();
    }

    public static Auction map(
            AuctionCreateRequest request,
            User seller,
            Category category,
            Manufacturer manufacturer,
            ItemCondition condition)
    {
        return Auction.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startPrice(request.getStartPrice())
                .category(category)
                .manufacturer(manufacturer)
                .condition(condition)
                .user(seller)
                .createdAt(LocalDateTime.now())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }
}
