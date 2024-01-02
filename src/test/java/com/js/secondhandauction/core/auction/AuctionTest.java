package com.js.secondhandauction.core.auction;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.exception.DuplicateUserTickException;
import com.js.secondhandauction.core.auction.exception.NotOverMinBidException;
import com.js.secondhandauction.core.auction.service.AuctionService;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.exception.AlreadySoldoutException;
import com.js.secondhandauction.core.item.exception.NotFoundItemException;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.exception.NotOverTotalBalanceException;
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
        String[] participant = {"1번", "2번", "3번", "4번", "5번", "6번", "7번", "8번"};

        long[] userId = new long[participant.length];

        for (int i=0; i< participant.length; i++) {
            User user = new User();
            user.setName(participant[i]);

            userId[i] = userService.create(user);
        }
        return userId;

    }

    long[] regItem() {

        String[] itemName = {"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8", "item9", "item10"};

        long[] itemId = new long[itemName.length];
        for (int i=0; i< itemName.length; i++) {
            Item item = new Item();
            item.setRegId(1L);
            item.setItem(itemName[i]);
            item.setRegPrice(400000);

            itemId[i] = itemService.create(item);
        }

        return itemId;
    }

    @Test
    @DisplayName("최고입찰자 또 입찰")
    @Transactional
    void auctionFailTest_duplicateUser() {
        long[] userId = regUser();
        long[] itemId = regItem();

        //duplicateAuction(itemId[0], userId[0]);

        assertThrows(DuplicateUserTickException.class,
                () -> duplicateAuction(itemId[0], userId[0]));
    }

    void duplicateAuction(long itemId, long userId) {
        Auction auction1 = new Auction();
        auction1.setItemNo(itemId);
        auction1.setRegId(userId);
        auction1.setBid(500000);

        Auction auction2 = new Auction();
        auction2.setItemNo(itemId);
        auction2.setRegId(userId);
        auction2.setBid(1500000);


        auctionService.create(auction1);
        auctionService.create(auction2);
    }

    @Test
    @DisplayName("입찰금액 총금액 초과")
    @Transactional
    void auctionFailTest_accessAmount() {
        long[] userId = regUser();
        long[] itemId = regItem();

        //overAmount(itemId[0], itemId[1], userId[0]);

        assertThrows(NotOverTotalBalanceException.class,
                () -> overAmount(itemId[1], itemId[2], userId[1]));
    }

    void overAmount(long itemId1, long itemId2, long userId) {
        Auction auction1 = new Auction();
        auction1.setItemNo(itemId1);
        auction1.setRegId(userId);
        auction1.setBid(5000000);

        Auction auction2 = new Auction();
        auction2.setItemNo(itemId2);
        auction2.setRegId(userId);
        auction2.setBid(6000000);

        auctionService.create(auction1);
        auctionService.create(auction2);
    }

    @Test
    @DisplayName("종료된 item에 입찰")
    @Transactional
    void auctionFailTest_endAuction() {
        long[] userId = regUser();
        long[] itemId = regItem();

        auctionSuccess(itemId[3], userId[2], userId[3]);

        //auctionService.create(itemId[0], userId[2], 1500000);

        Auction auction = new Auction();
        auction.setItemNo(itemId[3]);
        auction.setRegId(userId[4]);
        auction.setBid(1500000);

        assertThrows(AlreadySoldoutException.class,
                () -> auctionService.create(auction));
    }

    void auctionSuccess(long itemId, long userId1, long userId2) {
        int bid = 500000;

        Item item = itemService.get(itemId).orElseThrow(NotFoundItemException::new);

        for(int i=0; i<item.getBetTime(); i ++){
            Auction auction1 = new Auction();
            auction1.setItemNo(itemId);
            auction1.setRegId(userId1);
            auction1.setBid(bid);

            Auction auction2 = new Auction();
            auction2.setItemNo(itemId);
            auction2.setRegId(userId2);
            auction2.setBid(bid);


            if(i%2 == 0){
                auctionService.create(auction1);
            }else{
                auctionService.create(auction2);
            }

            System.out.println(userService.get(userId1).toString());
            System.out.println(userService.get(userId2).toString());

            bid += 100000;
        }
    }

    @Test
    @DisplayName("최소 입찰 금액 초과")
    @Transactional
    void auctionFailTest_minAmount() {
        long[] userId = regUser();
        long[] itemId = regItem();

        //auctionService.create(itemId[0], userId[0], 104000);

        Auction auction = new Auction();
        auction.setItemNo(itemId[4]);
        auction.setRegId(userId[5]);
        auction.setBid(104000);

        assertThrows(NotOverMinBidException.class,
                () -> auctionService.create(auction));
    }


}
