package com.componentbid.bid.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BidCreateRequest {
    private BigDecimal sum;
}
