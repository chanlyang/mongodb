package com.leon.mongodb.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.leon.mongodb.model.test.User;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

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
            user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            user.setUpdate_time(new Date());
            user.setIsDel(0);

            User result1 = userRepository.insert(user);
        }

        User user = new User();
        user.setUserId("00");
        user.setName("chenliang");
        user.setSex("男");
        user.setMoneyAmount(new BigDecimal("1000.001"));
        user.setMoneyAmount2(3222.1232);
        user.setMongAmountFen(10003000023l);
        user.setAge(11);
        user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        user.setUpdate_time(new Date());
        user.setIsDel(0);

        userRepository.save(user);
    }

    @Test
    public void baseFunc() {
        Gson gson = new Gson();

        //查询全部
        List<User> result = userRepository.findAll();
        System.out.println(gson.toJson(result));

        //插入
        User user1 = new User();
        user1.setUserId("0001");
        user1.setName("xx11");

        User user = userRepository.insert(user1);

        //批量插入
        User user2 = new User();
        user2.setUserId("0002");
        user2.setName("xx22");
        List<User> users = Lists.newArrayList(user1, user2);

        List<User> result1 = userRepository.insert(users);

        //查询全部并排序
        List<User> sortUsers = userRepository.findAll(Sort.sort(User.class).by(User::getAge).descending());


        //分页查询
        Page<User> pageUsers = userRepository.findAll(PageRequest.of(1, 20, Sort.sort(User.class).descending()));

        pageUsers.iterator().forEachRemaining(it -> {
            System.out.println(it);
        });

        //使用Example查询
        Example<User> example = Example.of(user1);
        List<User> users1 = userRepository.findAll(example);


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", endsWith())
                .withMatcher("name", startsWith().ignoreCase());

        Example<User> example2 = Example.of(user1, matcher);
        Long userCount = userRepository.count(example);

    }

    @Test
    public void testQuery1() {
        Gson gson = new Gson();

        //自定义查询
        User result1 = userRepository.findByName("chenliang");
        System.out.println(gson.toJson(result1));

    }


}