package com.componentbid.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class AuctionPreviewDto {
    private UUID id;
    private String title;
    private String imageUrl;
}