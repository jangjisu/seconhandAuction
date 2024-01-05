package com.js.secondhandauction.core.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class User {
    private long id;
    private String name;
    private int totalBalance = 10000000;
    private LocalDateTime regDate;
    private LocalDateTime uptDate;

    @Builder
    public User(long id,
                String name,
                int totalBalance,
                LocalDateTime regDate,
                LocalDateTime uptDate
    ) {
        this.id = id;
        this.name = name;
        this.totalBalance = totalBalance;
        this.regDate = regDate;
        this.uptDate = uptDate;
    }
}
