package com.componentbid.auction.service;

import com.componentbid.auction.dto.AuctionCreateRequest;
import com.componentbid.auction.dto.AuctionDetailsDto;
import com.componentbid.auction.dto.AuctionFilterRequest;
import com.componentbid.auction.dto.AuctionListItemDto;
import com.componentbid.auction.entity.*;
import com.componentbid.auction.mapper.AuctionMapper;
import com.componentbid.auction.repository.AuctionRepository;
import com.componentbid.auction.repository.CategoryRepository;
import com.componentbid.auction.repository.ItemConditionRepository;
import com.componentbid.auction.repository.ManufacturerRepository;
import com.componentbid.auction.specification.AuctionCriteria;
import com.componentbid.common.dto.ClassifierDto;
import com.componentbid.file.entity.FileMetadata;
import com.componentbid.file.service.IFileService;
import com.componentbid.user.entity.User;
import com.componentbid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
                .orElseThrow(() -> new IllegalArgumentException("Invalid category."));

        Manufacturer manufacturer = manufacturerRepository
                .findById(request.getManufacturerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid manufacturer."));

        ItemCondition condition = conditionRepository
                .findById(request.getConditionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid condition."));

        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user."));

        var auction = AuctionMapper.map(request, seller, category, manufacturer, condition);

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

    public Collection<AuctionListItemDto> getAuctions(AuctionFilterRequest filter) {

        Specification<Auction> specification = Specification.allOf();

        if (filter != null){
            specification = Specification
                    .allOf(
                            AuctionCriteria.hasCategory(filter.getCategoryId()),
                            AuctionCriteria.hasManufacturer(filter.getManufacturerId()),
                            AuctionCriteria.hasCondition(filter.getConditionId()),
                            AuctionCriteria.isActive()
                    );
        }

        return auctionRepository.findAll(specification)
                .stream()
                .map(AuctionMapper::projectListItem)
                .toList();
    }

    public AuctionDetailsDto getAuctionDetails(UUID auctionId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        return AuctionMapper.projectDetails(auction);
    }

    public List<ClassifierDto> getCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new ClassifierDto(c.getCategoryId(), c.getCategoryName()))
                .toList();
    }

    public List<ClassifierDto> getManufacturers() {
        return manufacturerRepository.findAll().stream()
                .map(m -> new ClassifierDto(m.getManufacturerId(), m.getManufacturerName()))
                .toList();
    }

    public List<ClassifierDto> getConditions() {
        return conditionRepository.findAll().stream()
                .map(c -> new ClassifierDto(c.getConditionId(), c.getConditionName()))
                .toList();
    }
}
