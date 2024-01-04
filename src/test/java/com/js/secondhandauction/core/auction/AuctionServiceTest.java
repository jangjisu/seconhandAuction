package com.js.secondhandauction.core.auction;

import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.exception.DuplicateUserTickException;
import com.js.secondhandauction.core.auction.exception.NotOverMinBidException;
import com.js.secondhandauction.core.auction.repository.AuctionRepository;
import com.js.secondhandauction.core.auction.service.AuctionService;
import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.domain.State;
import com.js.secondhandauction.core.item.exception.AlreadySoldoutException;
import com.js.secondhandauction.core.item.repository.ItemRepository;
import com.js.secondhandauction.core.item.service.ItemService;
import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.exception.NotOverTotalBalanceException;
import com.js.secondhandauction.core.user.repository.UserRepository;
import com.js.secondhandauction.core.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
public class AuctionServiceTest {

    @InjectMocks
    private AuctionService auctionService;
    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;


    public static long USER_ID = 1L;

    public static long ITEM_ID = 2L;

    private Auction auction;


    @BeforeEach
    public void setup() {
        User user = new User();
        user.setId(USER_ID);
        user.setName("Test User");

        Item item = new Item();
        item.setItemNo(ITEM_ID);
        item.setItem("Test Item");
        item.setState(State.ONSALE);
        item.setBetTime(5);
        item.setRegPrice(300000);

        auction = new Auction();
        auction.setBid(500000);
        auction.setItemNo(ITEM_ID);
        auction.setRegId(USER_ID);

        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(user);
        Mockito.when(itemService.getItem(Mockito.anyLong())).thenReturn(item);
    }

    @Test
    @DisplayName("입찰을 한다.")
    void testCreateAuction() {

        Mockito.when(auctionRepository.getCountTick(Mockito.anyLong())).thenReturn(0);

        Auction createdAuction = auctionService.createAuction(auction);
        assertNotNull(createdAuction);
        assertThat(createdAuction.getBid()).isEqualTo(500000);


    }

    @Test
    @DisplayName("한 유저가 어떤아이템에 중복 입찰을 해 입찰에 실패한다.")
    void testCreateAuctionThrowDuplicateUserTickException() {

        Mockito.when(auctionRepository.getCountTick(Mockito.anyLong())).thenReturn(1);

        Auction lastTick = new Auction();
        lastTick.setBid(400000);
        lastTick.setRegId(USER_ID);
        lastTick.setItemNo(ITEM_ID);

        Mockito.when(auctionRepository.getLastTick(Mockito.anyLong())).thenReturn(lastTick);

        Assertions.assertThrows(DuplicateUserTickException.class,
                () -> auctionService.createAuction(auction));

    }

    @Test
    @DisplayName("최소 베팅금액을 넘지 못해 입찰에 실패한다.")
    void testCreateAuctionThrowNotOverMinBidException() {

        Mockito.when(auctionRepository.getCountTick(Mockito.anyLong())).thenReturn(1);

        Auction lastTick = new Auction();
        lastTick.setBid(400000);
        lastTick.setRegId(100L);
        lastTick.setItemNo(ITEM_ID);

        Mockito.when(auctionRepository.getLastTick(Mockito.anyLong())).thenReturn(lastTick);

        auction.setBid(410000);

        Assertions.assertThrows(NotOverMinBidException.class,
                () -> auctionService.createAuction(auction));

    }


}
