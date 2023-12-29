package com.js.secondhandauction.core.auction;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.service.AuctionService;
import com.js.secondhandauction.core.item.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuctionServiceTest {

    @Autowired
    AuctionService auctionService;

    @Test
    @DisplayName("경매 생성 테스트")
    void createAuction() {
        Auction auction = new Auction();

        auction.setItem_no(1L);
        auction.setPrice(70000);
        auction.setReg_id(1L);

        auctionService.create(1L, 1L, 70000);
    }


}
