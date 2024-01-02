package com.js.secondhandauction.core.user.service;

import com.js.secondhandauction.core.user.domain.User;
import com.js.secondhandauction.core.user.exception.CannotTotalBalanceMinusException;
import com.js.secondhandauction.core.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
     * 회원 조회
     */
    public Optional<User> get(long id) {
        return Optional.ofNullable(userRepository.get(id));
    }

    /**
     * 회원 가진금액 더하기
     */
    public void plusAmount(long id, int totalBalance) {
        if(userRepository.get(id).getTotalBalance() + totalBalance > 0) {
            userRepository.updateTotalBalance(id, totalBalance);
        }else{
            throw new CannotTotalBalanceMinusException();
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
            throw new CannotTotalBalanceMinusException();
        }
    }
}
