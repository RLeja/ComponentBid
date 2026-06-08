package com.componentbid.auction.specification;

import com.componentbid.auction.entity.Auction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuctionCriteria {
    public static Specification<Auction> hasCategory(UUID categoryId) {
        if (categoryId == null) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Auction> hasManufacturer(UUID manufacturerId) {
        if (manufacturerId == null) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.get("manufacturer").get("id"), manufacturerId);
    }

    public static Specification<Auction> hasCondition(UUID conditionId) {
        if (conditionId == null) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.get("condition").get("id"), conditionId);
    }

    public static Specification<Auction> isActive() {
        LocalDateTime now = LocalDateTime.now();

        return (root, query, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get("startDate"), now),
                cb.greaterThanOrEqualTo(root.get("endDate"), now)
        );
    }
}
