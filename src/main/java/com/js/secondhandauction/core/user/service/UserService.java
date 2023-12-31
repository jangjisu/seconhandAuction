package com.js.secondhandauction.core.user.service;

import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 회원가입
     */
    public long create(User user) {
        userRepository.create(user);
        return user.getId();
    }

    /**
     * 회원가입
     */
    public long create(String name) {
        User user = new User();
        user.setName(name);

        userRepository.create(user);
        return user.getId();
    }

    /**
     * 회원가입
     */
    public long create(String name, int totalBalance) {
        User user = new User();
        user.setName(name);
        user.setTotalBalance(totalBalance);

        userRepository.create(user);
        return user.getId();
    }

    /**
     * 회원 조회
     */
    public User get(long id) {
        return userRepository.get(id);
    }

    /**
     * 회원 가진금액 더하기
     */
    public void plusAmount(long id, int totalBalance) {
        if(userRepository.get(id).getTotalBalance() + totalBalance > 0) {
            userRepository.updateTotalBalance(id, totalBalance);
        }else{
            throw new IllegalArgumentException("가진 금액이 마이너스가 될 수 없습니다");
        }
    }

    /**
     * 회원 가진금액 빼기
     */
    public void minusAmount(long id, int totalBalance) {
        if(userRepository.get(id).getTotalBalance() - totalBalance > 0) {
            totalBalance = -1 * totalBalance;
            userRepository.updateTotalBalance(id, totalBalance);
        }else{
            throw new IllegalArgumentException("가진 금액이 마이너스가 될 수 없습니다");
        }
    }
}
