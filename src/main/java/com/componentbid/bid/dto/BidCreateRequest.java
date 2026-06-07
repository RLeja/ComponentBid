package com.componentbid.bid.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BidCreateRequest {
    @NotNull(message = "Bid amount is required.")
    @DecimalMin(value = "0.01", message = "Bid amount must be at least 0.01.")
    private BigDecimal sum;
}
