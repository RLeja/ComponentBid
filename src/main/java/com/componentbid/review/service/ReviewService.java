package com.componentbid.review.service;

import com.componentbid.auction.entity.Auction;
import com.componentbid.auction.repository.AuctionRepository;
import com.componentbid.bid.entity.Bid;
import com.componentbid.bid.repository.BidRepository;
import com.componentbid.review.dto.ReviewCreateRequest;
import com.componentbid.review.entity.Review;
import com.componentbid.review.repository.ReviewRepository;
import com.componentbid.user.entity.User;
import com.componentbid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    public final AuctionRepository auctionRepository;
    public final ReviewRepository reviewRepository;
    public final BidRepository bidRepository;
    public final UserRepository userRepository;

    @Override
    public void createReview(
            UUID auctionId,
            UUID reviewerId,
            ReviewCreateRequest request) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow();

        if (auction.isActive()) {
            throw new IllegalStateException(
                    "Auction has not ended."
            );
        }

        if (reviewRepository.existsByAuction_Id(
                auctionId)) {

            throw new IllegalStateException(
                    "Review already exists."
            );
        }

        Bid winningBid =
                bidRepository
                        .findTopByAuction_IdOrderBySumDesc(
                                auctionId)
                        .orElseThrow();

        User winner = winningBid.getUser();

        if (!winner.getId().equals(reviewerId)) {
            throw new IllegalStateException(
                    "Only winner may review seller."
            );
        }

        Review review = new Review();

        review.setAuction(auction);

        review.setReviewer(winner);

        review.setSeller(
                auction.getUser()
        );

        review.setRating(
                request.getRating()
        );

        review.setComment(
                request.getComment()
        );

        review.setCreatedAt(
                LocalDateTime.now()
        );

        reviewRepository.save(review);

        updateSellerRating(
                auction.getUser()
        );
    }

    public boolean canReview(
            UUID auctionId,
            UUID userId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow();
        if(auction.isActive() || auction.getBids().isEmpty()){
            return false;
        }

        Bid winningBid =
                bidRepository
                        .findTopByAuction_IdOrderBySumDesc(
                                auctionId)
                        .orElseThrow();

        User winner = winningBid.getUser();

        return !reviewRepository.existsByAuction_Id(
                auctionId) && winner.getId().equals(userId);
    }

    private void updateSellerRating(User seller) {

        List<Review> reviews =
                reviewRepository.findBySeller_Id(
                        seller.getId()
                );

        double average =
                reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0);

        seller.setAverageRating(
                average
        );

        userRepository.save(
                seller
        );
    }
}
