package com.leon.mongodb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author: chenliang
 * @Date: 2021年01月13
 */
@Getter
@Setter
@Validated
//@ConfigurationProperties(prefix = "mongodb")
public class MongoClientOptionProperties {

    /**
     * 基础连接参数
     */
    private String database;
    private String username;
    private String password;
    @NotNull
    private List<String> address;
    private String authenticationDatabase;

    /**
     * 客户端连接池参数
     */
    @NotNull
    @Size(min = 1)
    private String clientName;
    /**
     * socket连接超时时间
     */
    @Min(value = 1)
    private int connectionTimeoutMs;
    /**
     * socket读取超时时间
     */
    @Min(value = 1)
    private int readTimeoutMs;
    /**
     * 连接池获取链接等待时间
     */
    @Min(value = 1)
    private int poolMaxWaitTimeMs;
    /**
     * 连接闲置时间
     */
    @Min(value = 1)
    private int connectionMaxIdleTimeMs;
    /**
     * 连接最多可以使用多久
     */
    @Min(value = 1)
    private int connectionMaxLifeTimeMs;
    /**
     * 心跳检测发送频率
     */
    @Min(value = 2000)
    private int heartbeatFrequencyMs;

    /**
     * 最小的心跳检测发送频率
     */
    @Min(value = 300)
    private int minHeartbeatFrequencyMs;
    /**
     * 计算允许多少个线程阻塞等待时的乘数，算法：threadsAllowedToBlockForConnectionMultiplier*connectionsPerHost
     */
    @Min(value = 1)
    private int threadsAllowedToBlockForConnectionMultiplier;
    /**
     * 心跳检测连接超时时间
     */
    @Min(value = 200)
    private int heartbeatConnectionTimeoutMs;
    /**
     * 心跳检测读取超时时间
     */
    @Min(value = 200)
    private int heartbeatReadTimeoutMs;

    /**
     * 每个host最大连接数
     */
    @Min(value = 1)
    private int connectionsPerHost;
    /**
     * 每个host的最小连接数
     */
    @Min(value = 1)
    private int minConnectionsPerHost;
}

