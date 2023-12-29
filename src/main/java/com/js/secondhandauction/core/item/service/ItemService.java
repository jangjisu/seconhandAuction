package com.js.secondhandauction.core.item.service;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.repository.ItemRepository;
import com.js.secondhandauction.core.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    ItemRepository itemRepository;

    UserService userService;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * 상품등록
     */
    public Long create(String itemName, int price, Long reg_id) {
        Item item = new Item();
        item.setItem(itemName);
        item.setReg_price(price);
        item.setReg_id(reg_id);
        item.setState(State.ONSALE);
        item.setBet((int) (Math.random() * 16) + 5);

        itemRepository.create(item);
        return item.getItem_no();
    }

    /**
     * 상품조회
     */
    public Item get(Long item_no) {
        return itemRepository.get(item_no);
    }

    /**
     * 상품 상태 확인
     */
     public boolean isOnSale(Long item_no) {
         return itemRepository.getState(item_no).equals(State.ONSALE);
     }

    /**
     * 상품상태 업데이트
     */
    public void updateState(Long item_no, State state) {
        itemRepository.updateState(item_no, state);
    }


}
