package com.js.secondhandauction.core.item.repository;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemRepository {
    long create(Item item);

    Item get(long item_no);

    State getState(long item_no);

    void updateState(Long item_no, State state);
}
