package com.js.secondhandauction.core.auction.service;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.repository.AuctionRepository;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
    public long create(long itemNo, long regId, int bid) {

        User user = userService.get(regId);
        if(user == null){
            throw new IllegalArgumentException("사용자가 존재하지 않음");
        }

        Item item = itemService.get(itemNo);
        //상수 Equals 변수 형식으로 변경
        if(State.SOLDOUT.equals(item.getState())){
            throw new IllegalArgumentException("아이템이 판매 종료됨");
        }

        if(user.getTotalBalance() < bid){
            throw new IllegalArgumentException("가진돈보다 더 큰 금액을 배팅함");
        }

        int countTick = auctionRepository.getCountTick(itemNo);

        boolean isTick = (countTick != 0);

        int minBid = 0;
        boolean isImmediatePurchase;
        Auction lastTick = null;

        if(isTick){
            lastTick = auctionRepository.getLastTick(itemNo);

            minBid = lastTick.getBid() + item.getRegPrice() * min_betting_percent / 100;

            isImmediatePurchase = (lastTick.getBid() * immediate_purchase_rate < bid);

            if(lastTick.getBid() > bid){
                throw new IllegalArgumentException("입찰 금액이 최고금액보다 더 작음");
            }
            if(lastTick.getRegId() == regId) {
                throw new IllegalArgumentException("최고입찰자가 또 입찰함");
            }
        }else{
            minBid = item.getRegPrice();

            isImmediatePurchase = (item.getRegPrice() * immediate_purchase_rate < bid);
        }

        if(bid < minBid){
            throw new IllegalArgumentException("최소 경매금액 아래 베팅");
        }

        userService.minusAmount(regId, bid);

        Auction auction = new Auction();
        auction.setItemNo(itemNo);
        auction.setBid(bid);
        auction.setRegId(regId);

        auctionRepository.create(auction);

        //bet -1 번째 일 경우 경매 종료
        if(countTick == item.getBetTime()-1 || isImmediatePurchase) {
            if(isTick){
                userService.plusAmount(lastTick.getRegId(), lastTick.getBid());
            }

            userService.plusAmount(item.getRegId(), bid);

            itemService.updateState(itemNo, State.SOLDOUT);

            System.out.println("입찰 종료");

        }

        return auction.getAuctionNo();


    }

}
