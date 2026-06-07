package com.componentbid.bid.dto;

import com.componentbid.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class BidDto {
    private UUID id;

    private BigDecimal sum;
    private LocalDateTime createdAt;

    private UserDto bidder;
}