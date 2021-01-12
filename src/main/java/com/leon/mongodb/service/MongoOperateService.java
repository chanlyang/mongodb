package com.leon.mongodb.service;

import com.leon.mongodb.model.test.User;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: chenliang
 * @Date: 2021年01月12
 */
@Service
public class MongoOperateService {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 创建对象
     *
     * @param user
     */
    public void saveUser(User user) {
        mongoTemplate.save(user);
    }

    /**
     * 根据用户名查询对象
     *
     * @param userName
     * @return
     */
    public User findUserByUserName(String userName) {
        Query query = new Query(Criteria.where("name").is(userName));
        User user = mongoTemplate.findOne(query, User.class);
        return user;
    }

    /**
     * 更新对象
     *
     * @param user
     */
    public long updateUser(User user) {
        Query query = new Query(Criteria.where("userId").is(user.getUserId()));
        Update update = new Update().set("name", user.getName()).set("age", user.getAge());
        //更新查询返回结果集的第一条
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,UserEntity.class);
        if (result != null) {
            return result.getMatchedCount();
        } else {
            return 0;
        }
    }

    /**
     * 删除对象
     *
     * @param userId
     */
    public void deleteUserById(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        mongoTemplate.remove(query, User.class);
    }
}
