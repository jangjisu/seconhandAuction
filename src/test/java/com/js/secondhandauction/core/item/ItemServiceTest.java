package com.js.secondhandauction.core.item;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.exception.NotFoundItemException;
import com.js.secondhandauction.core.item.repository.ItemRepository;
import com.js.secondhandauction.core.item.service.ItemService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    private Item item;

    @BeforeEach void setup() {
        item = new Item();

        item.setItem("Test Item");
        item.setRegId(1L);
        item.setRegPrice(200000);
    }

    @Test
    @DisplayName("상품을 생성한다")
    void testCreateItem() {
        when(itemRepository.create(any(Item.class))).thenReturn(anyLong());

        Item createdItem = itemService.createItem(item);

        assertNotNull(createdItem);
        Assertions.assertThat(State.ONSALE).isEqualTo(createdItem.getState());
        Assertions.assertThat(createdItem.getBetTime()).isBetween(5, 20);

        Mockito.verify(itemRepository, times(1)).create(any(Item.class));
    }

    @Test
    @DisplayName("상품을 조회한다")
    void testGetItem() {
        when(itemRepository.findByItemNo(anyLong())).thenReturn(Optional.ofNullable(item));

        Item getItem = itemService.getItem(1L);

        assertNotNull(getItem);
        Assertions.assertThat("Test Item").isEqualTo(getItem.getItem());
        Mockito.verify(itemRepository, times(1)).findByItemNo(anyLong());
    }

    @Test
    @DisplayName("상품이 존재하지 않아 조회에 실패한다")
    void testGetItemThrowNotFoundItemException() {
        when(itemRepository.findByItemNo(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundItemException.class,
        () -> itemService.getItem(1L));
    }

    @Test
    @DisplayName("상품 상태를 확인한다")
    void testIsOnSale() {
        when(itemRepository.getState(anyLong())).thenReturn(State.ONSALE);

        Assertions.assertThat(itemService.isItemOnSale(1L)).isEqualTo(true);
        Mockito.verify(itemRepository, times(1)).getState(anyLong());
    }

    @Test
    @DisplayName("상품 상태를 변경한다")
    void testUpdateState() {
        // 상품 생성
        when(itemRepository.create(any(Item.class))).thenReturn(anyLong());
        Item createdItem = itemService.createItem(item);

        // 상태 변경
        when(itemRepository.updateState(anyLong(), any(State.class))).thenReturn(1);
        itemService.updateItemState(createdItem.getItemNo(), State.SOLDOUT);

        // 상태 확인
        when(itemRepository.getState(anyLong())).thenReturn(State.SOLDOUT); // getState() 메소드를 Mocking
        Assertions.assertThat(itemService.isItemOnSale(createdItem.getItemNo())).isEqualTo(false);

        // Mocking된 메소드가 호출되었는지 검증
        Mockito.verify(itemRepository, times(1)).updateState(anyLong(), any(State.class));
        Mockito.verify(itemRepository, times(1)).getState(anyLong());
    }
}
