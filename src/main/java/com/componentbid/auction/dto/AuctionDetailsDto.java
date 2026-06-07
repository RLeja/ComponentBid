package com.componentbid.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class AuctionDetailsDto {
    private UUID id;
    private String title;
    private String description;

    private String categoryName;
    private String manufacturerName;
    private String conditionName;

    private BigDecimal startPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean active;

    private UUID sellerId;
    private String sellerName;

    private Collection<String> imageUrls;
}
