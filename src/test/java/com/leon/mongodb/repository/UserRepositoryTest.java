package com.leon.mongodb.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.leon.mongodb.model.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Resource
    private UserRepository userRepository;

    private Random ran = new Random();
    private final static int delta = 0x9fa5 - 0x4e00 + 1;

    public char getRandomHan() {
        return (char) (0x4e00 + ran.nextInt(delta));
    }


    @Test
    public void testInsert() {

        for (int i = 1; i < 10; i++) {
            User user = new User();
            user.setUserId(Joiner.on("").join("00", i));
            user.setName(Joiner.on("").join(getRandomHan(), getRandomHan()));
            if (i % 2 == 0) {
                user.setSex("男");
            } else {
                user.setSex("女");
            }
            user.setMoneyAmount(new BigDecimal(ran.nextDouble()));
            user.setMoneyAmount2(ran.nextGaussian());
            user.setMongAmountFen(ran.nextLong());
            user.setAge(ran.nextInt());
            //user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            user.setUpdate_time(new Date());
            user.setIsDel(0);

            User result1 = userRepository.insert(user);
        }
    }

    @Test
    public void testQuery1() {
        Gson gson = new Gson();
        List<User> result = userRepository.findAll();
        System.out.println(gson.toJson(result));
    }


}