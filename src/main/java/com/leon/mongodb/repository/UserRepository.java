package com.leon.mongodb.repository;

import com.leon.mongodb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: chenliang
 * @Date: 2021年01月12
 */
@Repository("userRepository")
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * 根据用户名查询
     * @param name
     * @return
     */
    User findByName(String name);

}
