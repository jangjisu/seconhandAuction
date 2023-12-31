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
        Auction auction = new Auction();
        auction.setItemNo(itemNo);
        auction.setBid(bid);
        auction.setRegId(regId);



        User user = userService.get(regId);
        if(user == null){
            throw new IllegalArgumentException("사용자가 존재하지 않음");
        }

        Item itemforSale = itemService.get(itemNo);
        if(itemforSale.getState().equals(State.SOLDOUT)){
            throw new IllegalArgumentException("아이템이 판매 종료됨");
        }

        if(user.getTotalBalance() < bid){
            throw new IllegalArgumentException("가진돈보다 더 큰 금액을 배팅함");
        }

        Auction maxAuction = auctionRepository.getMax(itemNo);

        int priceForCompare = (maxAuction == null) ? itemforSale.getRegPrice() * (100 + min_betting_percent) / 100 : maxAuction.getBid() + itemforSale.getRegPrice() * min_betting_percent / 100;

        if(bid < priceForCompare){
            throw new IllegalArgumentException("최소 경매금액 아래 베팅");
        }

        boolean immediate_purchase_YN;
        if(maxAuction == null){
            immediate_purchase_YN = (itemforSale.getRegPrice() * immediate_purchase_rate < auction.getBid());
        }else{
            immediate_purchase_YN = (maxAuction.getBid() * immediate_purchase_rate < auction.getBid());
        }




        if(maxAuction.getBid() > bid){
            throw new IllegalArgumentException("입찰 금액이 최고금액보다 더 작음");
        }
        if(maxAuction.getRegId() == regId) {
            throw new IllegalArgumentException("최고입찰자가 또 입찰함");
        }

        //배팅자 없거나, 이미 배팅된 금액중 가장 큰 금액인지, 가장 큰 금액 베팅자 본인인지 확인

        userService.minusAmount(regId, bid);

        auctionRepository.create(auction);

        int count = auctionRepository.getCount(itemNo);
        //bet -1 번째 일 경우 경매 종료
        if(count == itemforSale.getBetTime()-1 || immediate_purchase_YN) {
            if(maxAuction != null){
                userService.plusAmount(maxAuction.getRegId(), maxAuction.getBid());
            }

            userService.plusAmount(itemforSale.getRegId(), bid);

            itemService.updateState(itemNo, State.SOLDOUT);

            System.out.println("입찰 종료");

        }

        return auction.getAuctionNo();


    }

}
