package com.js.secondhandauction.core.auction.service;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.exception.DuplicateUserTickException;
import com.js.secondhandauction.core.auction.exception.NotOverMinBidException;
import com.js.secondhandauction.core.auction.repository.AuctionRepository;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.exception.AlreadySoldoutException;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.exception.NotOverTotalBalanceException;
import com.js.secondhandauction.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuctionService {

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    final int MIN_BETTING_PERCENT = 5;

    final int IMMEDIATE_PURCHASE_RATE_START_PRICE = 3;
    final int IMMEDIATE_PURCHASE_RATE_LAST_BID = 2;


    /**
     * 경매 등록
     */
    @Transactional
    public Auction createAuction(Auction auction) {

        validateUser(auction);

        Item item = validateItem(auction);

        int countTick = auctionRepository.getCountTick(auction.getItemNo());

        boolean isTick = (countTick != 0);

        boolean isImmediatePurchase;
        Auction lastTick = null;

        if (isTick) {
            lastTick = validateBidForExistingTicks(auction, item.getRegPrice());
            isImmediatePurchase = isImmediatelyPurchasableByLastBid(lastTick.getBid(), auction.getBid());
        } else {
            validateBidForInitialBid(item, auction.getBid());
            isImmediatePurchase = isImmediatelyPurchasableByRegPrice(item.getRegPrice(), auction.getBid());
        }

        updateUserBalanceAndCreateAuction(auction, lastTick);

        if (isFinalBid(countTick, item.getBetTime()) || isImmediatePurchase) {
            finishAuction(auction, item);
        }

        return auction;
    }

    private void validateUser(Auction auction) {
        User user = userService.getUser(auction.getRegId());

        if (user.getTotalBalance() < auction.getBid()) {
            throw new NotOverTotalBalanceException();
        }

        //return user;
    }

    private Item validateItem(Auction auction) {
        Item item = itemService.getItem(auction.getItemNo());

        //상수 Equals 변수 형식으로 변경
        if (State.SOLDOUT.equals(item.getState())) {
            throw new AlreadySoldoutException();
        }

        return item;

    }

    private Auction validateBidForExistingTicks(Auction auction, int regPrice) {
        Auction lastTick = auctionRepository.getLastTick(auction.getItemNo());

        if (lastTick.getRegId() == auction.getRegId()) {
            throw new DuplicateUserTickException();
        }

        int minBid = lastTick.getBid() + (regPrice * MIN_BETTING_PERCENT / 100);

        if (auction.getBid() < minBid) {
            throw new NotOverMinBidException();
        }

        return lastTick;

    }

    private void validateBidForInitialBid(Item item, int bid) {
        int minBid = item.getRegPrice();

        if (bid < minBid) {
            throw new NotOverMinBidException();
        }
    }

    private boolean isImmediatelyPurchasableByRegPrice(int regPrice, int bid) {
        return regPrice * IMMEDIATE_PURCHASE_RATE_START_PRICE < bid;
    }

    private boolean isImmediatelyPurchasableByLastBid(int lastBid, int bid) {
        return lastBid * IMMEDIATE_PURCHASE_RATE_LAST_BID < bid;
    }


    private void updateUserBalanceAndCreateAuction(Auction auction, Auction lastTick) {
        userService.updateUserTotalBalance(auction.getRegId(), auction.getBid() * -1);

        if (lastTick != null) {
            userService.updateUserTotalBalance(lastTick.getRegId(), lastTick.getBid());
        }

        auctionRepository.create(auction);
    }

    private boolean isFinalBid(int countTick, int betTime) {
        return betTime - 1 == countTick;
    }

    private void finishAuction(Auction auction, Item item) {
        userService.updateUserTotalBalance(item.getRegId(), auction.getBid());

        itemService.updateItemState(item.getItemNo(), State.SOLDOUT);

        //log.debug("{}({}) 입찰 종료", item.getItem(), itemNo);
        log.debug(item.getItem() + "(" + item.getItemNo() + ") 입찰 종료");
    }

}
