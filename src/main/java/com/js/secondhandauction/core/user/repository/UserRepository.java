package com.js.secondhandauction.core.user.repository;

import com.js.secondhandauction.core.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {
    long create(User user);

    User get(long id);

    void updateTotalBalance(long id, int totalBalance);

}
