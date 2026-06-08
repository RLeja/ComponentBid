package com.componentbid.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class AuctionListItemDto {
    private UUID id;
    private String title;
    private String description;
    private String categoryName;
    private String conditionName;
    private String manufacturerName;
    private BigDecimal startPrice;
    private String currentStatus;
    private String imageUrl;
}
