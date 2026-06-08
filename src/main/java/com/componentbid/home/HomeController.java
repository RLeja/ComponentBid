package com.componentbid.home;

import com.componentbid.auction.dto.AuctionFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.componentbid.auction.service.AuctionService;

import java.util.UUID;

//@RestController
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AuctionService auctionService;

    //TODO: Pagination
    @GetMapping("/")
    public String home(@ModelAttribute AuctionFilterRequest filter, Model model) {

        model.addAttribute("auctions", auctionService.getAuctions(filter));
        model.addAttribute("filter", filter);

        model.addAttribute("categories", auctionService.getCategories());
        model.addAttribute("manufacturers", auctionService.getManufacturers());
        model.addAttribute("conditions", auctionService.getConditions());

        return "index";
    }
}
