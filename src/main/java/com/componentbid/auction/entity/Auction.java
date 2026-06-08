package com.componentbid.auction.entity;

import com.componentbid.file.entity.FileMetadata;
import com.componentbid.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.componentbid.user.entity.User;
import com.componentbid.bid.entity.Bid;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "condition_id", nullable = false)
    private ItemCondition condition;

    @Column(nullable = false)
    private BigDecimal startPrice;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "auction_image",
            joinColumns = @JoinColumn(name = "auction_id"),
            inverseJoinColumns = @JoinColumn(name = "fileMetadata_id")
    )
    private Collection<FileMetadata> images;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    @OneToOne(mappedBy = "auction", cascade = CascadeType.ALL)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public AuctionStatus getCurrentStatus() {

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startDate)) {
            return AuctionStatus.UPCOMING;
        }

        if (now.isAfter(endDate)) {
            return AuctionStatus.ENDED;
        }

        return AuctionStatus.ACTIVE;
    }

    public boolean isActive() {
        return getCurrentStatus() == AuctionStatus.ACTIVE;
    }
}
