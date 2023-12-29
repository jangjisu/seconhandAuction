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
    public Long create(User user) {
        userRepository.create(user);
        return user.getId();
    }

    /**
     * 회원가입-V2
     */
    public Long create(String name) {
        User user = new User();
        user.setName(name);

        userRepository.create(user);
        return user.getId();
    }

    /**
     * 회원가입-V2
     */
    public Long create(String name, int amount) {
        User user = new User();
        user.setName(name);
        user.setAmount(amount);

        userRepository.create(user);
        return user.getId();
    }

    /**
     * 회원 조회
     */
    public User get(Long id) {
        return userRepository.get(id);
    }

    /**
     * 회원 가진금액 더하기
     */
    public void plusAmount(Long id, int amount) {
        if(userRepository.get(id).getAmount() + amount > 0) {
            userRepository.plusAmount(id, amount);
        }else{
            throw new IllegalArgumentException("가진 금액이 마이너스가 될 수 없습니다");
        }
    }

    /**
     * 회원 가진금액 빼기
     */
    public void minusAmount(Long id, int amount) {
        if(userRepository.get(id).getAmount() - amount > 0) {
            userRepository.minusAmount(id, amount);
        }else{
            throw new IllegalArgumentException("가진 금액이 마이너스가 될 수 없습니다");
        }
    }
}
