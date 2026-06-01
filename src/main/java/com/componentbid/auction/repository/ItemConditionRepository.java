package com.componentbid.auction.repository;

import com.componentbid.auction.entity.ItemCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ItemConditionRepository extends JpaRepository<ItemCondition, UUID> {
    Optional<ItemCondition> findByConditionName(String itemConditionName);
}
