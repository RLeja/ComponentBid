package com.componentbid.auction.service;

import com.componentbid.auction.dto.AuctionCreateRequest;
import com.componentbid.auction.dto.AuctionDetailsDto;
import com.componentbid.auction.entity.*;
import com.componentbid.auction.repository.AuctionRepository;
import com.componentbid.auction.repository.CategoryRepository;
import com.componentbid.auction.repository.ItemConditionRepository;
import com.componentbid.auction.repository.ManufacturerRepository;
import com.componentbid.file.entity.FileMetadata;
import com.componentbid.file.service.IFileService;
import com.componentbid.user.entity.User;
import com.componentbid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuctionService implements IAuctionService {
    private final IFileService fileService;

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
        //auction.setImageUrl(request.getImageUrl());

        auction.setCategory(category);
        auction.setManufacturer(manufacturer);
        auction.setCondition(condition);

        auction.setUser(seller);

        //auction.setStatus(String.valueOf(AuctionStatus.ACTIVE));

        auction.setCreatedAt(LocalDateTime.now());

        auction.setStartDate(request.getStartDate());
        auction.setEndDate(request.getEndDate());

        List<FileMetadata> images = new ArrayList<>();

        for (MultipartFile image : request.getImages()) {
            if (image.isEmpty()) continue;

            try {
                images.add(fileService.save(image));
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image: " + image.getOriginalFilename(), e);
            }
        }

        auction.setImages(images);
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

    public AuctionDetailsDto getAuctionDetails(UUID auctionId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        return AuctionDetailsDto.builder() //TODO: Refactor to mapper
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
                .sellerId(auction.getUser().getId())
                .sellerName(auction.getUser().getName())
                .imageUrls(
                        auction.getImages()
                                .stream()
                                .map(image -> "/files/" + image.getId())
                                .toList()
                )
                .build();
    }
}
