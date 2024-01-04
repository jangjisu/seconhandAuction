package com.js.secondhandauction.core.item.repository;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ItemRepository {
    long create(Item item);

    Optional<Item> findByItemNo(long itemNo);

    State getState(long itemNo);

    int updateState(long itemNo, State state);
}
