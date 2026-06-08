package com.componentbid.review.mapper;

import com.componentbid.review.dto.ReviewDto;
import com.componentbid.review.entity.Review;
import com.componentbid.user.mapper.UserMapper;

public class ReviewMapper {
    public static ReviewDto project(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .seller(UserMapper.projectUser(review.getSeller()))
                .build();
    }
}
