package com.js.secondhandauction.core.auction.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Auction {
    private long auctionNo;
    private long itemNo;
    private int bid = 0;
    private long regId;
    private String regDate;
}
