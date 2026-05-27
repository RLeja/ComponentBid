package com.componentbid.bid.repository;

import com.componentbid.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BidRepository extends JpaRepository<User, UUID> {
}
