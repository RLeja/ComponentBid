package com.componentbid.bid.repository;

import com.componentbid.bid.entity.Bid;
import com.componentbid.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {
    List<Bid> findByAuction_IdOrderBySumDesc(UUID auctionId);
    Optional<Bid> findTopByAuction_IdOrderBySumDesc(UUID auctionId);
}
