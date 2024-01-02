package com.js.secondhandauction.core.item;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.service.ItemService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Test
    @DisplayName("상품 생성 테스트")
    @Transactional
    void createItem() {
        //Item item = new Item();


        //item.setItem("에어팟");
        //item.setRegId(1L);
        //item.setState(State.ONSALE);
        //item.setRegPrice(10000);

        long itemNo = itemService.create("에어팟", 200000, 1L);

        //System.out.println(itemNo);

    }

    @Test
    @DisplayName("상품 조회 테스트")
    void getItem() {
        Item item = itemService.get(1L);


    }

    @Test
    @DisplayName("상품 State 확인")
    void isOnSale() {
        boolean saleYN = itemService.isOnSale(1L);

        Assertions.assertThat(saleYN).isEqualTo(true);
    }

    @Test
    @DisplayName("상품 State 변경")
    void updateState() {
        itemService.updateState(1L, State.ONSALE);

        Item item = itemService.get(1L);

        Assertions.assertThat(item.getState()).isEqualTo(State.ONSALE);

    }
}
