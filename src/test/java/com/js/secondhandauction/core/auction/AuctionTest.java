package com.js.secondhandauction.core.auction;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.service.AuctionService;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuctionTest {

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    AuctionService auctionService;

    List<Long> registUser() {
        String[] participant = {"1번", "2번", "3번", "4번"};

        List<Long> user_id = new ArrayList<Long>();
        for (String s : participant) {
            user_id.add(userService.create(s));
        }
        return user_id;

    }

    List<Long> registItem() {
        
        String[] item = {"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8", "item9", "item10"};

        List<Long> item_id = new ArrayList<Long>();
        for (String s : item) {
            item_id.add(itemService.create(s, 400000, 1L));
        }

        return item_id;
    }

    @Test
    @DisplayName("최고입찰자 또 입찰")
    @Transactional
    void auctionFailTest_duplicateUser() {
        List<Long> user_id = registUser();
        List<Long> item_id = registItem();

        duplicateAuction(item_id.get(0), user_id.get(0));

        assertThrows(IllegalArgumentException.class,
                () -> duplicateAuction(item_id.get(0), user_id.get(0)));
    }

    void duplicateAuction(Long item_id, Long user_id) {
        auctionService.create(item_id, user_id, 500000);
        auctionService.create(item_id, user_id, 1500000);
    }

    @Test
    @DisplayName("입찰금액 총금액 초과")
    @Transactional
    void auctionFailTest_accessAmount() {
        List<Long> user_id = registUser();
        List<Long> item_id = registItem();

        overAmount(item_id.get(0), item_id.get(1), user_id.get(0));

        assertThrows(IllegalArgumentException.class,
                () -> overAmount(item_id.get(0), item_id.get(1), user_id.get(0)));
    }

    void overAmount(Long item_id1, Long item_id2, Long user_id) {
        auctionService.create(item_id1, user_id, 5000000);
        auctionService.create(item_id2, user_id, 6000000);
    }

    @Test
    @DisplayName("종료된 item에 입찰")
    @Transactional
    void auctionFailTest_endAuction() {
        List<Long> user_id = registUser();
        List<Long> item_id = registItem();

        auctionSuccess(item_id.get(0), user_id.get(0), user_id.get(1));

        //auctionService.create(item_id.get(0), user_id.get(2), 1500000);

        assertThrows(IllegalArgumentException.class,
                () -> auctionService.create(item_id.get(0), user_id.get(2), 1500000));
    }

    void auctionSuccess(Long item_id, Long user_id1, Long user_id2) {
        auctionService.create(item_id, user_id1, 500000);
        auctionService.create(item_id, user_id2, 600000);
        auctionService.create(item_id, user_id1, 700000);
        auctionService.create(item_id, user_id2, 800000);
        auctionService.create(item_id, user_id1, 900000);
        auctionService.create(item_id, user_id2, 1000000);
        auctionService.create(item_id, user_id1, 1100000);
        auctionService.create(item_id, user_id2, 1200000);
        auctionService.create(item_id, user_id1, 1300000);
        auctionService.create(item_id, user_id2, 1400000);
    }

    @Test
    @DisplayName("최소 입찰 금액 초과")
    @Transactional
    void auctionFailTest_minAmount() {
        List<Long> user_id = registUser();
        List<Long> item_id = registItem();

        //auctionService.create(item_id.get(0), user_id.get(0), 104000);

        assertThrows(IllegalArgumentException.class,
                () -> auctionService.create(item_id.get(0), user_id.get(0), 104000));
    }


}
