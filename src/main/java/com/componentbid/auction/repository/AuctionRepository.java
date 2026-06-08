package com.componentbid.auction.repository;

import com.componentbid.auction.entity.Auction;
import com.componentbid.user.entity.User;
import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuctionRepository extends JpaRepository<Auction, UUID>, JpaSpecificationExecutor<Auction> {
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"category", "manufacturer", "condition", "images"})
    List<Auction> findAll(Specification<Auction> spec);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"category", "manufacturer", "condition", "user", "images", "bids"})
    Optional<Auction> findById(@NonNull UUID id);
}
