package com.leon.mongodb.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: chenliang
 * @Date: 2021年01月13
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MongoClientOptionProperties.class)
public class MongoDBConfig {


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
    public CustomConversions customConversions() {
        List list = new ArrayList();
        list.add(new TimestampConverter());
        return new CustomConversions(list);
    }

    /**
     * 自定义mongo连接池
     *
     * @param properties
     * @return
     */
    @Bean
    public MongoDbFactory mongoDbFactory(MongoClientOptionProperties properties) {
        //创建客户端参数
        MongoClientOptions options = mongoClientOptions(properties);

        //创建客户端和Factory
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String address : properties.getAddress()) {
            String[] hostAndPort = address.split(":");
            String host = hostAndPort[0];
            Integer port = Integer.parseInt(hostAndPort[1]);
            ServerAddress serverAddress = new ServerAddress(host, port);
            serverAddresses.add(serverAddress);
        }

        //创建认证客户端
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(properties.getUsername(),
                properties.getAuthenticationDatabase() != null ? properties.getAuthenticationDatabase() : properties.getDatabase(),
                properties.getPassword().toCharArray());

        MongoClient mongoClient = new MongoClient(serverAddresses.get(0), mongoCredential, options);
        //集群模式
        if (serverAddresses.size() > 1) {
            mongoClient = new MongoClient(serverAddresses, new ArrayList<>(Arrays.asList(mongoCredential)));
        }
        /** ps: 创建非认证客户端*/
        //MongoClient mongoClient = new MongoClient(serverAddresses, mongoClientOptions);
        return new SimpleMongoDbFactory(mongoClient, properties.getDatabase());
    }

    /**
     * mongo客户端参数配置
     *
     * @return
     */
    public MongoClientOptions mongoClientOptions(MongoClientOptionProperties properties) {
        return MongoClientOptions.builder()
                .connectTimeout(properties.getConnectionTimeoutMs())
                .socketTimeout(properties.getReadTimeoutMs()).applicationName(properties.getClientName())
                .heartbeatConnectTimeout(properties.getHeartbeatConnectionTimeoutMs())
                .heartbeatSocketTimeout(properties.getHeartbeatReadTimeoutMs())
                .heartbeatFrequency(properties.getHeartbeatFrequencyMs())
                .minHeartbeatFrequency(properties.getMinHeartbeatFrequencyMs())
                .maxConnectionIdleTime(properties.getConnectionMaxIdleTimeMs())
                .maxConnectionLifeTime(properties.getConnectionMaxLifeTimeMs())
                .maxWaitTime(properties.getPoolMaxWaitTimeMs())
                .connectionsPerHost(properties.getConnectionsPerHost())
                .threadsAllowedToBlockForConnectionMultiplier(
                        properties.getThreadsAllowedToBlockForConnectionMultiplier())
                .minConnectionsPerHost(properties.getMinConnectionsPerHost()).build();
    }
}