package com.componentbid.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {

    @Min(0)
    @Max(5)
    private Integer rating;

    @NotBlank
    private String comment;
}
