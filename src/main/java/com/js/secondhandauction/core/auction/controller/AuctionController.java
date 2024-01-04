package com.js.secondhandauction.core.auction.controller;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping
    public ResponseEntity<Auction> createAuction(@RequestBody Auction auction) {
        return ResponseEntity.ok(auctionService.createAuction(auction));
    }

    @GetMapping("/{itemNo}")
    public ResponseEntity<List<Auction>> getAuctions(@PathVariable("itemNo") long itemNo) {
        return ResponseEntity.ok(auctionService.getAuctions(itemNo));
    }
}
