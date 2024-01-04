package com.js.secondhandauction.core.auction.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Auction {
    private long auctionNo;
    private long itemNo;
    private int bid = 0;
    private long regId;
    private LocalDateTime regDate;

    @Builder
    public Auction(long auctionNo,
                   long itemNo,
                   int bid,
                   long regId,
                   LocalDateTime regDate
    ) {
        this.auctionNo = auctionNo;
        this.itemNo = itemNo;
        this.bid = bid;
        this.regId = regId;
        this.regDate = regDate;
    }
}
