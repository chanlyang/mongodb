package com.leon.mongodb.service;

import com.leon.mongodb.model.test.User;
import com.mongodb.WriteResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        //is 查询
        Query query = new Query(Criteria.where("name").is(userName));
        User user = mongoTemplate.findOne(query, User.class);

        //正则查询
        query.addCriteria(Criteria.where("name").regex("^A"));
        List<User> users = mongoTemplate.find(query, User.class);

        //大于小于查询
        query.addCriteria(Criteria.where("age").lt(50).gt(20));

        //排序
        query.with(Sort.sort(User.class).by(User::getAge).ascending());

        //分页
        final Pageable pageableRequest = PageRequest.of(0, 2);
        query.with(pageableRequest);


        users = mongoTemplate.find(query, User.class);
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
        WriteResult result = mongoTemplate.updateFirst(query, update, User.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,UserEntity.class);
        if (result != null) {
            return result.getN();
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
