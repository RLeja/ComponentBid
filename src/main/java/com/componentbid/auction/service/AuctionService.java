package com.componentbid.auction.service;

import com.componentbid.auction.dto.AuctionCreateRequest;
import com.componentbid.auction.entity.*;
import com.componentbid.auction.repository.AuctionRepository;
import com.componentbid.auction.repository.CategoryRepository;
import com.componentbid.auction.repository.ItemConditionRepository;
import com.componentbid.auction.repository.ManufacturerRepository;
import com.componentbid.user.entity.User;
import com.componentbid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuctionService implements IAuctionService {
    private final AuctionRepository auctionRepository;

    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ItemConditionRepository conditionRepository;
    private final UserRepository userRepository;

    @Override
    public UUID createAuction(AuctionCreateRequest request, UUID userId) {

        if(request.getStartDate().isBefore(LocalDateTime.now()) || request.getEndDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException(
                    "Both start and end dates must be later than current date."
            );
        }

        if (!request.getEndDate().isAfter(request.getStartDate())) {
            throw new IllegalArgumentException(
                    "End date must be after start date."
            );
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow();

        Manufacturer manufacturer = manufacturerRepository
                .findById(request.getManufacturerId())
                .orElseThrow();

        ItemCondition condition = conditionRepository
                .findById(request.getConditionId())
                .orElseThrow();

        User seller = userRepository.findById(userId)
                .orElseThrow();

        Auction auction = new Auction();

        auction.setTitle(request.getTitle());
        auction.setDescription(request.getDescription());
        auction.setStartPrice(request.getStartPrice());
        auction.setImageUrl(request.getImageUrl());

        auction.setCategory(category);
        auction.setManufacturer(manufacturer);
        auction.setCondition(condition);

        auction.setUser(seller);

        //auction.setStatus(String.valueOf(AuctionStatus.ACTIVE));

        auction.setCreatedAt(LocalDateTime.now());

        auction.setStartDate(request.getStartDate());
        auction.setEndDate(request.getEndDate());

        auctionRepository.save(auction);

        return auction.getId();
    }

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }
    public List<Auction> getFilteredAuctions(UUID categoryId,
                                             UUID manufacturerId,
                                             UUID conditionId){
        return auctionRepository.findFiltered(categoryId, manufacturerId, conditionId);
    }
    public Auction getAuctionById(UUID auctionId) {

        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
    }
}
