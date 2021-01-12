package com.leon.mongodb.service;

import com.google.gson.Gson;
import com.leon.mongodb.model.test.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoOperateServiceTest {

    @Resource
    private MongoOperateService mongoOperateService;


    @Test
    public void testSaveUser() throws Exception {
        User user = new User();
        user.setUserId("00");
        user.setName("chenliang");
        user.setSex("男");
        user.setMoneyAmount(new BigDecimal("1000.001"));
        user.setMoneyAmount2(3222.1232);
        user.setMongAmountFen(10003000023l);
        user.setAge(11);
        //user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        user.setUpdate_time(new Date());
        user.setIsDel(0);

        mongoOperateService.saveUser(user);
    }

    @Test
    public void findUserByUserName() {
        User user = mongoOperateService.findUserByUserName("chenliang");
        Gson gson = new Gson();
        System.out.println(gson.toJson(user));

    }

    @Test
    public void updateUser() {
        User user = new User();
        user.setUserId("00");
        user.setName("XXXX");
        user.setAge(100);
        mongoOperateService.updateUser(user);
    }

    @Test
    public void deleteUserById() {
        User user = mongoOperateService.findUserByUserName("XXXX");
        mongoOperateService.deleteUserById(user.getUserId());
    }
}