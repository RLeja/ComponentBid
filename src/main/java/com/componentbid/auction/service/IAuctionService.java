package com.componentbid.auction.service;

import com.componentbid.auction.dto.AuctionCreateRequest;
import com.componentbid.auction.dto.AuctionDetailsDto;
import com.componentbid.auction.dto.AuctionFilterRequest;
import com.componentbid.auction.dto.AuctionListItemDto;
import com.componentbid.common.dto.ClassifierDto;

import java.util.Collection;
import java.util.UUID;

public interface IAuctionService {
    UUID createAuction(AuctionCreateRequest request, UUID userId);
    AuctionDetailsDto getAuctionDetails(UUID auctionId);
    Collection<AuctionListItemDto> getAuctions(AuctionFilterRequest filter);
    Collection<ClassifierDto> getCategories();
    Collection<ClassifierDto> getManufacturers();
    Collection<ClassifierDto> getConditions();
}
