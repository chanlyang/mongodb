package com.leon.mongodb.service;

import com.leon.mongodb.model.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: chenliang
 * @Date: 2021年01月12
 */
@Service
public class MongoOperateService {

    @Resource
    private MongoTemplate mongotemplate;

    public void testQuery() {
        Query query = new Query(Criteria.where("userId").is("001"));
        User user = mongotemplate.findOne(query, User.class);
    }
}
