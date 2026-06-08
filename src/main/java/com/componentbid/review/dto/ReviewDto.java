package com.componentbid.review.dto;

import com.componentbid.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ReviewDto {
    private UUID id;
    private Integer rating;
    private String comment;
    public UserDto seller;
}
