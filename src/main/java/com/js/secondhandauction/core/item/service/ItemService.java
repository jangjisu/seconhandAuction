package com.js.secondhandauction.core.item.service;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.dto.ItemCreateRequest;
import com.js.secondhandauction.core.item.exception.NotFoundItemException;
import com.js.secondhandauction.core.item.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    /**
     * 상품등록
     */
    public Item createItem(ItemCreateRequest itemCreateRequest) {
        Item item = itemCreateRequest.toEntity();

        item.setState(State.ONSALE);
        item.setBetTime((int) (Math.random() * 16) + 5);

        itemRepository.create(item);
        return item;
    }

    /**
     * 상품조회
     */
    public Item getItem(long itemNo) {
        return itemRepository.findByItemNo(itemNo).orElseThrow(NotFoundItemException::new);
    }

    /**
     * 상품 상태 확인
     */
    public boolean isItemOnSale(long itemNo) {
        return itemRepository.getState(itemNo).equals(State.ONSALE);
    }

    /**
     * 상품상태 업데이트
     */
    public void updateItemState(long itemNo, State state) {
        itemRepository.updateState(itemNo, state);
    }

}
