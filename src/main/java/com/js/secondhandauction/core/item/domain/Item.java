package com.js.secondhandauction.core.item.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Item {
    private long itemNo;
    private String item;
    private String regDate;
    private String uptDate;
    private int regPrice;
    private State state;
    private long regId;
    private int betTime;
}
