package com.componentbid.auction.repository;

import com.componentbid.auction.entity.Category;
import com.componentbid.auction.entity.ItemCondition;
import com.componentbid.auction.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, UUID> {
    Optional<Manufacturer> findByManufacturerName(String manufacturerName);
}
