package com.componentbid.review.service;

import com.componentbid.review.dto.ReviewCreateRequest;

import java.util.UUID;

public interface IReviewService {
    void createReview(
            UUID auctionId,
            UUID reviewerId,
            ReviewCreateRequest request);
}
