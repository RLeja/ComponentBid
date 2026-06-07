package com.componentbid.auction.repository;

import com.componentbid.auction.entity.Auction;
import com.componentbid.user.entity.User;
import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuctionRepository extends JpaRepository<Auction, UUID> {
    @Query("""
    SELECT a
    FROM Auction a
    WHERE (:categoryId IS NULL
           OR a.category.categoryId = :categoryId)
      AND (:manufacturerId IS NULL
           OR a.manufacturer.manufacturerId = :manufacturerId)
      AND (:conditionId IS NULL
           OR a.condition.conditionId = :conditionId)
""")
    List<Auction> findFiltered(
            UUID categoryId,
            UUID manufacturerId,
            UUID conditionId
    );

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"category", "manufacturer", "condition", "user", "images", "bids"})
    Optional<Auction> findById(@NonNull UUID id);
}
