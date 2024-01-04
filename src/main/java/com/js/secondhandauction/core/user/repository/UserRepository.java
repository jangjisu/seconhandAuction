package com.js.secondhandauction.core.user.repository;

import com.js.secondhandauction.core.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRepository {
    long create(User user);

    Optional<User> findById(long id);

    void updateTotalBalance(long id, int totalBalance);

}
