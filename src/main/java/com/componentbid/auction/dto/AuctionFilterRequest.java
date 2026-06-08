package com.componentbid.auction.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class AuctionFilterRequest {
    UUID categoryId;
    UUID manufacturerId;
    UUID conditionId;
}
