package com.componentbid.config;

import com.componentbid.auction.entity.Auction;
import com.componentbid.auction.entity.Category;
import com.componentbid.auction.entity.ItemCondition;
import com.componentbid.auction.entity.Manufacturer;
import com.componentbid.auction.repository.AuctionRepository;
import com.componentbid.auction.repository.CategoryRepository;
import com.componentbid.auction.repository.ItemConditionRepository;
import com.componentbid.auction.repository.ManufacturerRepository;
import com.componentbid.bid.entity.Bid;
import com.componentbid.bid.repository.BidRepository;
import com.componentbid.review.entity.Review;
import com.componentbid.review.repository.ReviewRepository;
import com.componentbid.user.entity.Role;
import com.componentbid.user.entity.User;
import com.componentbid.user.entity.UserStatus;
import com.componentbid.user.repository.RoleRepository;
import com.componentbid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ItemConditionRepository conditionRepository;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final ReviewRepository reviewRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {
            return;
        }
        //comment to reset db

        // ROLES

        Role userRole = new Role();
        userRole.setName("USER");

        roleRepository.save(userRole);

        // CATEGORIES

        Category gpu = createCategory("Graphics Cards");
        Category cpu = createCategory("Processors");
        Category motherboard = createCategory("Motherboards");
        Category ram = createCategory("Memory");
        Category storage = createCategory("Storage");

        // MANUFACTURERS

        Manufacturer nvidia = createManufacturer("NVIDIA");
        Manufacturer amd = createManufacturer("AMD");
        Manufacturer intel = createManufacturer("Intel");
        Manufacturer asus = createManufacturer("ASUS");
        Manufacturer samsung = createManufacturer("Samsung");

        // CONDITIONS

        ItemCondition newCondition = createCondition("New");
        ItemCondition likeNew = createCondition("Like New");
        ItemCondition used = createCondition("Used");

        // USERS

        User john = createUser(
                "john@example.com",
                "John",
                4.8,
                userRole
        );

        User alice = createUser(
                "alice@example.com",
                "Alice",
                4.6,
                userRole
        );

        User bob = createUser(
                "bob@example.com",
                "Bob",
                4.2,
                userRole
        );

        User emma = createUser(
                "emma@example.com",
                "Emma",
                4.9,
                userRole
        );

        User mike = createUser(
                "mike@example.com",
                "Mike",
                4.5,
                userRole
        );

        // ACTIVE AUCTIONS

        Auction rtx4070 = createAuction(
                "RTX 4070 Ti",
                "Excellent GPU",
                gpu,
                nvidia,
                likeNew,
                john,
                BigDecimal.valueOf(500),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(5)
        );

        Auction ryzen7800 = createAuction(
                "Ryzen 7 7800X3D",
                "Gaming CPU",
                cpu,
                amd,
                newCondition,
                alice,
                BigDecimal.valueOf(300),
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(4)
        );

        // FUTURE AUCTION

        createAuction(
                "Intel i9-14900K",
                "Brand new",
                cpu,
                intel,
                newCondition,
                bob,
                BigDecimal.valueOf(450),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(8)
        );

        // ENDED AUCTIONS

        Auction rtx3080 = createAuction(
                "RTX 3080 Founders Edition",
                "Used but works perfectly",
                gpu,
                nvidia,
                used,
                john,
                BigDecimal.valueOf(350),
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(1)
        );

        Auction x670 = createAuction(
                "ASUS X670E Hero",
                "High-end motherboard",
                motherboard,
                asus,
                used,
                emma,
                BigDecimal.valueOf(150),
                LocalDateTime.now().minusDays(8),
                LocalDateTime.now().minusDays(2)
        );

        createAuction(
                "Corsair RM850x",
                "No bids auction",
                storage,
                samsung,
                used,
                mike,
                BigDecimal.valueOf(80),
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(1)
        );

        // BIDS

        createBid(rtx4070, bob, 550);
        createBid(rtx4070, emma, 600);
        createBid(rtx4070, mike, 650);

        createBid(ryzen7800, john, 320);
        createBid(ryzen7800, mike, 350);

        createBid(rtx3080, alice, 400);
        createBid(rtx3080, bob, 450);
        createBid(rtx3080, emma, 500);

        createBid(x670, john, 180);
        createBid(x670, mike, 220);

        // REVIEWS

        Review review1 = new Review();
        review1.setAuction(rtx3080);
        review1.setReviewer(emma);
        review1.setSeller(john);
        review1.setRating(5);
        review1.setCreatedAt(LocalDateTime.now());
        review1.setComment(
                "Great seller, item matched description."
        );

        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setAuction(x670);
        review2.setReviewer(mike);
        review2.setSeller(emma);
        review2.setRating(4);
        review2.setCreatedAt(LocalDateTime.now());
        review2.setComment(
                "Fast communication and shipping."
        );

        reviewRepository.save(review2);
    }

    private Category createCategory(String name) {
        Category c = new Category();
        c.setCategoryName(name);
        return categoryRepository.save(c);
    }

    private Manufacturer createManufacturer(String name) {
        Manufacturer m = new Manufacturer();
        m.setManufacturerName(name);
        return manufacturerRepository.save(m);
    }

    private ItemCondition createCondition(String name) {
        ItemCondition c = new ItemCondition();
        c.setConditionName(name);
        return conditionRepository.save(c);
    }

    private User createUser(
            String email,
            String name,
            double rating,
            Role role) {

        User user = new User();

        user.setEmail(email);
        user.setName(name);
        user.setPasswordHash(
                passwordEncoder.encode("password")
        );
        user.setAverageRating(rating);
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(List.of(role));

        return userRepository.save(user);
    }

    private Auction createAuction(
            String title,
            String description,
            Category category,
            Manufacturer manufacturer,
            ItemCondition condition,
            User seller,
            BigDecimal startPrice,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        Auction auction = new Auction();

        auction.setTitle(title);
        auction.setDescription(description);
        auction.setImageUrl(defaultAuctionImageUrl(title));
        auction.setCategory(category);
        auction.setManufacturer(manufacturer);
        auction.setCondition(condition);
        auction.setUser(seller);
        auction.setStartPrice(startPrice);
        auction.setStartDate(startDate);
        auction.setEndDate(endDate);
        auction.setCreatedAt(LocalDateTime.now());

        return auctionRepository.save(auction);
    }

    private String defaultAuctionImageUrl(String title) {
        return "https://placehold.co/640x480/f8fafc/1f2937?text=" +
                URLEncoder.encode(title, StandardCharsets.UTF_8);
    }

    private void createBid(
            Auction auction,
            User bidder,
            double amount) {

        Bid bid = new Bid();

        bid.setAuction(auction);
        bid.setUser(bidder);
        bid.setSum(BigDecimal.valueOf(amount));
        bid.setCreatedAt(LocalDateTime.now());

        bidRepository.save(bid);
    }
}
