package com.js.secondhandauction.core.user.service;

import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.exception.CannotTotalBalanceMinusException;
import com.js.secondhandauction.core.user.exception.NotFoundUserException;
import com.js.secondhandauction.core.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    /**
     * 회원가입
     */
    public User createUser(User user) {
        long id = userRepository.create(user);
        //user.setId(id);
        return user;
    }

    /**
     * 회원 조회
     */
    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(NotFoundUserException::new);
    }

    /**
     * 회원 가진금액 더하기
     */
    public void updateUserTotalBalance(long id, int totalBalance) {
        if (userRepository.findById(id).orElseThrow(NotFoundUserException::new).getTotalBalance() + totalBalance > 0) {
            userRepository.updateTotalBalance(id, totalBalance);
        } else {
            throw new CannotTotalBalanceMinusException();
        }
    }
}
