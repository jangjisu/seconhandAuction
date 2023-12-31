package com.js.secondhandauction.core.auction;

import com.js.secondhandauction.core.auction.service.AuctionService;
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
        //Auction auction = new Auction();

        //auction.setItemNo(1L);
        //auction.setBid(70000);
        //auction.setRegId(1L);

        auctionService.create(1L, 1L, 70000);
    }


}
