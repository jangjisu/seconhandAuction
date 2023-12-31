package com.js.secondhandauction.core.auction;

import com.js.secondhandauction.core.auction.service.AuctionService;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuctionTest {

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    AuctionService auctionService;

    long[] regUser() {
        String[] participant = {"1번", "2번", "3번", "4번"};

        long[] userId = new long[participant.length];

        for (int i=0; i< participant.length; i++) {
            userId[i] = userService.create(participant[i]);
        }
        return userId;

    }

    long[] regItem() {

        String[] item = {"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8", "item9", "item10"};

        long[] itemId = new long[item.length];
        for (int i=0; i< item.length; i++) {
            itemId[i] = itemService.create(item[i], 400000, 1L);
        }

        return itemId;
    }

    @Test
    @DisplayName("최고입찰자 또 입찰")
    @Transactional
    void auctionFailTest_duplicateUser() {
        long[] userId = regUser();
        long[] itemId = regItem();

        duplicateAuction(itemId[0], userId[0]);

        assertThrows(IllegalArgumentException.class,
                () -> duplicateAuction(itemId[0], userId[0]));
    }

    void duplicateAuction(long itemId, long userId) {
        auctionService.create(itemId, userId, 500000);
        auctionService.create(itemId, userId, 1500000);
    }

    @Test
    @DisplayName("입찰금액 총금액 초과")
    @Transactional
    void auctionFailTest_accessAmount() {
        long[] userId = regUser();
        long[] itemId = regItem();

        overAmount(itemId[0], itemId[1], userId[0]);

        assertThrows(IllegalArgumentException.class,
                () -> overAmount(itemId[0], itemId[1], userId[0]));
    }

    void overAmount(long itemId1, long itemId2, long userId) {
        auctionService.create(itemId1, userId, 5000000);
        auctionService.create(itemId2, userId, 6000000);
    }

    @Test
    @DisplayName("종료된 item에 입찰")
    @Transactional
    void auctionFailTest_endAuction() {
        long[] userId = regUser();
        long[] itemId = regItem();

        auctionSuccess(itemId[0], userId[0], userId[1]);

        //auctionService.create(itemId[0], userId[2], 1500000);

        assertThrows(IllegalArgumentException.class,
                () -> auctionService.create(itemId[0], userId[2], 1500000));
    }

    void auctionSuccess(long itemId, long userId1, long userId2) {
        auctionService.create(itemId, userId1, 500000);
        auctionService.create(itemId, userId2, 600000);
        auctionService.create(itemId, userId1, 700000);
        auctionService.create(itemId, userId2, 800000);
        auctionService.create(itemId, userId1, 900000);
        auctionService.create(itemId, userId2, 1000000);
        auctionService.create(itemId, userId1, 1100000);
        auctionService.create(itemId, userId2, 1200000);
        auctionService.create(itemId, userId1, 1300000);
        auctionService.create(itemId, userId2, 1400000);
    }

    @Test
    @DisplayName("최소 입찰 금액 초과")
    @Transactional
    void auctionFailTest_minAmount() {
        long[] userId = regUser();
        long[] itemId = regItem();

        //auctionService.create(itemId[0], userId[0], 104000);

        assertThrows(IllegalArgumentException.class,
                () -> auctionService.create(itemId[0], userId[0], 104000));
    }


}
