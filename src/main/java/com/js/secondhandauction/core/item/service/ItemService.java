package com.js.secondhandauction.core.item.service;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * 상품등록
     */
    public long create(String itemName, int price, long regId) {
        Item item = new Item();
        item.setItem(itemName);
        item.setRegPrice(price);
        item.setRegId(regId);
        item.setState(State.ONSALE);
        item.setBetTime((int) (Math.random() * 16) + 5);

        itemRepository.create(item);
        return item.getItemNo();
    }

    /**
     * 상품조회
     */
    public Item get(long itemNo) {
        return itemRepository.get(itemNo);
    }

    /**
     * 상품 상태 확인
     */
    public boolean isOnSale(long itemNo) {
        return itemRepository.getState(itemNo).equals(State.ONSALE);
    }

    /**
     * 상품상태 업데이트
     */
    public void updateState(long itemNo, State state) {
        itemRepository.updateState(itemNo, state);
    }


}