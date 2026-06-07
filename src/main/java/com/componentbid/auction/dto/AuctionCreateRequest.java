package com.componentbid.auction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class AuctionCreateRequest {
    @NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title must be 255 characters or less.")
    private String title;

    @NotBlank(message = "Description is required.")
    @Size(max = 2000, message = "Description must be 2000 characters or less.")
    private String description;

    @NotNull(message = "Category is required.")
    private UUID categoryId;

    @NotNull(message = "Manufacturer is required.")
    private UUID manufacturerId;

    @NotNull(message = "Condition is required.")
    private UUID conditionId;

    @NotNull(message = "Starting price is required.")
    @DecimalMin(value = "0.01", message = "Starting price must be at least 0.01.")
    private BigDecimal startPrice;

    @NotNull(message = "Condition is required.")
    private List<MultipartFile> images;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Start date is required.")
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "End date is required.")
    private LocalDateTime endDate;

//    @NotNull
//    private UUID userId;
}
