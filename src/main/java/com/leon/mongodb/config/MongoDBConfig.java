package com.leon.mongodb.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: chenliang
 * @Date: 2021年01月13
 */
@Slf4j
@Configuration
//@EnableConfigurationProperties(MongoClientOptionProperties.class)
public class MongoDBConfig {

    @Value("${mongodb.host}")
    private String host;
    @Value("${mongodb.port}")
    private Integer port;
    @Value("${mongodb.database}")
    private String database;

    /**
     * 自定义mongo连接池
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {

        MongoClient mongoClient = new MongoClient(host, port);

        return new SimpleMongoDbFactory(mongoClient, database);
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomConversions customConversions() {
        List list = new ArrayList();
        list.add(new TimestampConverter());
        return new CustomConversions(list);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory, CustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
        //去掉默认mapper添加的_class
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        //添加自定义的转换器
        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory dbFactory, MappingMongoConverter converter) {
        MongoTemplate mongoTemplate = new MongoTemplate(dbFactory, converter);
        return mongoTemplate;
    }





}