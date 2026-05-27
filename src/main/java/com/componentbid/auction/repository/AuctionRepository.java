package com.componentbid.auction.repository;

import com.componentbid.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuctionRepository extends JpaRepository<User, UUID> {
}
