package com.componentbid.bid.service;

import com.componentbid.auction.entity.Auction;
import com.componentbid.auction.repository.AuctionRepository;
import com.componentbid.bid.entity.Bid;
import com.componentbid.bid.repository.BidRepository;
import com.componentbid.user.entity.User;
import com.componentbid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BidService implements IBidService {
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public void placeBid(
            UUID auctionId,
            UUID userId,
            BigDecimal sum) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow();

        User user = userRepository.findById(userId)
                .orElseThrow();

        if (!auction.isActive()) {
            throw new IllegalStateException(
                    "Cannot bid on an inactive auction."
            );
        }

        if (auction.getUser().getId()
                .equals(userId)) {

            throw new IllegalStateException(
                    "Seller cannot bid on own auction."
            );
        }

        Bid bid = new Bid();

        BigDecimal highestBid =
                bidRepository.findByAuction_IdOrderBySumDesc(auctionId)
                        .stream()
                        .findFirst()
                        .map(Bid::getSum)
                        .orElse(auction.getStartPrice());

        if (sum.compareTo(highestBid) <= 0) {
            throw new IllegalArgumentException(
                    "Bid must be higher than current bid."
            );
        }

        bid.setAuction(auction);
        bid.setUser(user);
        bid.setSum(sum);
        bid.setCreatedAt(LocalDateTime.now());

        bidRepository.save(bid);
    }
}
