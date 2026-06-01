package com.componentbid.auction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "item_conditions")
public class ItemCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID conditionId;

    @Column(nullable = false, unique = true)
    private String conditionName;
}