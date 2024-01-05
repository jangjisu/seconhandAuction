package com.js.secondhandauction.core.item.service;

import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.dto.ItemRequest;
import com.js.secondhandauction.core.item.dto.ItemResponse;
import com.js.secondhandauction.core.item.exception.ItemException;
import com.js.secondhandauction.core.item.exception.NotFoundItemException;
import com.js.secondhandauction.core.item.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.Duration;
import java.time.LocalDateTime;

import static com.js.secondhandauction.core.item.domain.State.*;

@Service
@Slf4j
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    final int addDay = 1;

    /**
     * 상품등록
     */
    public ItemResponse createItem(long userId, ItemRequest itemRequest) {
        Item item = itemRequest.toEntity();

        item.setRegId(userId);
        item.setState(ONSALE);
        item.setBetTime((int) (Math.random() * 16) + 5);

        itemRepository.create(item);
        return item.toResponse();
    }

    /**
     * 상품조회
     */
    public Item getItem(long itemNo) {
        return itemRepository.findByItemNo(itemNo).orElseThrow(NotFoundItemException::new);
    }

    /**
     * 상품수정
     */
    public ItemResponse updateItem(long itemNo, long userId, ItemRequest itemRequest) {
        Item item = itemRequest.toEntity();
        item.setItemNo(itemNo);
        item.setRegId(userId);

        State itemState = getItemState(itemNo);

        switch (itemState) {
            case ONSALE:
                item.setState(State.ONSALE);

                itemRepository.updateForOnsale(item);
                break;
            case UNSOLD:
                item.setState(State.ONSALE);
                item.setBetTime((int) (Math.random() * 16) + 5);

                itemRepository.updateForUnsold(item);
                break;
            case SOLDOUT:
                throw new ItemException(ErrorCode.CANNOT_UPDATE_SOLDOUT_ITEM);
            default:
                throw new ItemException(ErrorCode.NOT_FOUND_ITEM);
        }

        return item.toResponse();
    }

    /**
     * 상품 상태 확인
     */
    public State getItemState(long itemNo) {
        return itemRepository.getState(itemNo);
    }

    /**
     * 상품상태 업데이트
     */
    public void updateItemState(long itemNo, State state) {
        itemRepository.updateState(itemNo, state);
    }

    /**
     * 스케줄러를 통한 아이템 상태 업데이트
     */
    public void updateStateCheckItems() {
        itemRepository.getStateCheckItems(addDay).stream().forEach(itemExpirationCheck -> {
            if (itemExpirationCheck.getLastTick() == null) {
                updateItemState(itemExpirationCheck.getItemNo(), State.UNSOLD);
                log.debug("상품번호: {} 상태: {} 로 변경", itemExpirationCheck.getItemNo(), State.UNSOLD);
            } else if (LocalDateTime.now().isAfter(itemExpirationCheck.getLastTick().plusHours(24L * addDay))) {
                updateItemState(itemExpirationCheck.getItemNo(), State.SOLDOUT);
                log.debug("상품번호: {} 상태: {} 로 변경", itemExpirationCheck.getItemNo(), State.SOLDOUT);
            }
        });
    }
}
