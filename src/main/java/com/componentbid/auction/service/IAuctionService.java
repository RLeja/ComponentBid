package com.componentbid.auction.service;

import com.componentbid.auction.dto.AuctionCreateRequest;

import java.util.UUID;

public interface IAuctionService {
    UUID createAuction(AuctionCreateRequest request, UUID userId);
}
