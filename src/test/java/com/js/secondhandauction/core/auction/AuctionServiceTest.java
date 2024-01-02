package com.js.secondhandauction.core.auction;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.service.AuctionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class AuctionServiceTest {

    @Autowired
    AuctionService auctionService;

    @Test
    @DisplayName("경매 생성 테스트")
    @Transactional
    void createAuction() {
        Auction auction = new Auction();

        auction.setItemNo(3L);
        auction.setBid(200000);
        auction.setRegId(1L);

        auctionService.create(auction);
    }


}
