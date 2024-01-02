package com.js.secondhandauction.core.user;

import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.exception.CannotTotalBalanceMinusException;
import com.js.secondhandauction.core.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;


    @Test
    @DisplayName("유저 생성 테스트")
    @Transactional
    void createUser() {
        User user = new User();
        //user.setAmount(1000000);
        user.setName("test2");

        long id = userService.create(user);

        //System.out.println("******* - id = " + user.getId());
        //System.out.println("======" + userService.getUser(user.getId()).getId());
        Assertions.assertThat(id).isEqualTo(userService.get(id).getId());

    }

    @Test
    @DisplayName("유저 조회 테스트")
    @Transactional
    void getUser() {
        long id = 9L;

        User user = userService.get(id);

        Assertions.assertThat(user.getTotalBalance()).isEqualTo(10000000);
    }

    @Test
    @DisplayName("유저 조회 실패 테스트")
    void getFailUser() {
        long id = 10L;

        User user = userService.get(id);

        Assertions.assertThat(user).isEqualTo(null);
    }

    @Test
    @DisplayName("금액 업데이트 성공 테스트")
    @Transactional
    void updateAmount() {
        User user1 = userService.get(1L);

        userService.plusAmount(1L, -500000);

        User user2 = userService.get(1L);

        Assertions.assertThat(user1.getTotalBalance()-500000).isEqualTo(user2.getTotalBalance());
    }

    @Test
    @DisplayName("금액 업데이트 실패 테스트")
    void updateFailAmount() {
        //userService.plusAmount(1L, -50000000);

        assertThrows(CannotTotalBalanceMinusException.class,
                () -> userService.plusAmount(1L, -50000000));
    }

}
