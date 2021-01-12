package com.leon.mongodb.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoOperateServiceTest {

    @Resource
    private MongoTemplate mongotemplate;

    @Test
    public void testQuery(){
        Query query = new Query();
        System.out.println();
    }

}