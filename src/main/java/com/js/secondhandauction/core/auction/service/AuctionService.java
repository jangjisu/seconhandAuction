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
import com.js.secondhandauction.core.user.exception.NotFoundUserException;
import com.js.secondhandauction.core.user.exception.NotOverTotalBalanceException;
import com.js.secondhandauction.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuctionService {

    AuctionRepository auctionRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    final int min_betting_percent = 5;

    final int immediate_purchase_rate = 3;

public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    /**
     * 경매 등록
     */
    @Transactional
    public long create(long itemNo, long regId, int bid) {

        User user = userService.get(regId);
        if(user == null){
            throw new NotFoundUserException();
        }

        Item item = itemService.get(itemNo);
        //상수 Equals 변수 형식으로 변경
        if(State.SOLDOUT.equals(item.getState())){
            throw new AlreadySoldoutException();
        }

        if(user.getTotalBalance() < bid){
            throw new NotOverTotalBalanceException();
        }

        int countTick = auctionRepository.getCountTick(itemNo);

        boolean isTick = (countTick != 0);

        int minBid;
        boolean isImmediatePurchase;
        Auction lastTick = null;

        if(isTick){
            lastTick = auctionRepository.getLastTick(itemNo);

            minBid = lastTick.getBid() + item.getRegPrice() * min_betting_percent / 100;

            isImmediatePurchase = (lastTick.getBid() * immediate_purchase_rate < bid);

            if(lastTick.getBid() > bid){
                throw new NotOverMinBidException();
            }
            if(lastTick.getRegId() == regId) {
                throw new DuplicateUserTickException();
            }
        }else{
            minBid = item.getRegPrice();

            isImmediatePurchase = (item.getRegPrice() * immediate_purchase_rate < bid);
        }

        if(bid < minBid){
            throw new NotOverMinBidException();
        }

        userService.minusAmount(regId, bid);

        if(isTick){
            userService.plusAmount(lastTick.getRegId(), lastTick.getBid());
        }

        Auction auction = new Auction();
        auction.setItemNo(itemNo);
        auction.setBid(bid);
        auction.setRegId(regId);

        auctionRepository.create(auction);

        //bet -1 번째 일 경우 경매 종료
        if(countTick == item.getBetTime()-1 || isImmediatePurchase) {

            userService.plusAmount(item.getRegId(), bid);

            itemService.updateState(itemNo, State.SOLDOUT);

            log.debug(item.getItem() + "(" + itemNo + ") 입찰 종료");

        }

        return auction.getAuctionNo();


    }

}
