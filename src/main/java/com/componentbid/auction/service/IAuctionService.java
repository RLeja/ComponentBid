package com.componentbid.auction.service;

import com.componentbid.auction.dto.*;
import com.componentbid.common.dto.ClassifierDto;

import java.util.Collection;
import java.util.UUID;

public interface IAuctionService {
    UUID createAuction(AuctionCreateRequest request, UUID userId);
    void updateAuction(UUID auctionId, AuctionUpdateRequest request, UUID userId, Boolean isAdmin);
    AuctionDetailsDto getAuctionDetails(UUID auctionId);
    AuctionUpdateRequest getAuctionEditForm(UUID auctionId);
    void deleteAuction(UUID auctionId, UUID userId, boolean isAdmin);

    Collection<AuctionListItemDto> getAuctions(AuctionFilterRequest filter);
    Collection<ClassifierDto> getCategories();
    Collection<ClassifierDto> getManufacturers();
    Collection<ClassifierDto> getConditions();
}
