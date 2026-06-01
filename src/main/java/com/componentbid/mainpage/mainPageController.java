package com.componentbid.mainpage;

import com.componentbid.auction.entity.Auction;
import com.componentbid.auction.repository.CategoryRepository;
import com.componentbid.auction.repository.ItemConditionRepository;
import com.componentbid.auction.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.componentbid.auction.service.AuctionService;

import java.util.UUID;

//@RestController
@Controller
@RequiredArgsConstructor
public class mainPageController {
    private final AuctionService auctionService;

    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ItemConditionRepository conditionRepository;

    @GetMapping("/")
    public String home(@RequestParam(required = false) UUID categoryId,
                       @RequestParam(required = false) UUID manufacturerId,
                       @RequestParam(required = false) UUID conditionId,
                       Model model) {
        model.addAttribute(
                "auctions",
                auctionService.getFilteredAuctions(
                        categoryId,
                        manufacturerId,
                        conditionId
                ).stream()
                 .filter(Auction::isActive)
                 .toList()
        );

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("manufacturers", manufacturerRepository.findAll());
        model.addAttribute("conditions", conditionRepository.findAll());

        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedManufacturerId", manufacturerId);
        model.addAttribute("selectedConditionId", conditionId);

        return "index";
    }
}
