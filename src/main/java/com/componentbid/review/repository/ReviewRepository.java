package com.componentbid.review.repository;

import com.componentbid.review.entity.Review;
import com.componentbid.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsByAuction_Id(UUID auctionId);

    List<Review> findBySeller_Id(UUID sellerId);
}
