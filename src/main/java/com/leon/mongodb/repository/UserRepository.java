package com.leon.mongodb.repository;

import com.leon.mongodb.model.test.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: chenliang
 * @Date: 2021年01月12
 */
@Repository("userRepository")
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * 根据用户名查询
     *
     * @param name
     * @return
     */
    User findByName(String name);

    /**
     * 年龄求和
     *
     * @return
     */
    Long countByAge();

    /**
     * 注解查询
     *
     * @param regexp
     * @return
     */
    @Query("{ 'name' : { $regex: ?0 } }")
    List<User> findUsersByRegexpName(String regexp);

    /**
     * 根据姓名查，结果集根据年龄倒序
     *
     * @param name
     * @return
     */
    List<User> findByNameOrderByAgeDesc(String name);

    /**
     * 查询年龄在i 和 j直接的
     *
     * @param i
     * @param j
     * @return
     */
    List<User> findByAgeBetween(int i, int j);

    /**
     * 注解查询
     * @param ageGT
     * @param ageLT
     * @return
     */
    @Query("{ 'age' : { $gt: ?0, $lt: ?1 } }")
    List<User> findUsersByAgeBetween(int ageGT, int ageLT);

}
