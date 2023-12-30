package com.js.secondhandauction.core.user.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private long id;
    private String name;
    private int totalBalance = 10000000;
    private String regDate;
    private String uptDate;
}
