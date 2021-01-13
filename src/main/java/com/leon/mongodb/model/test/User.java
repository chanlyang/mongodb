package com.leon.mongodb.model.test;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author: chenliang
 * @Date: 2021年01月12
 *
 * 声明为一个文档
 */
@Document
@Data
@CompoundIndexes(
        @CompoundIndex(name = "idx_user_id_name", def = "{'userId':1, 'name':1}", unique = true)
)
public class User {

    /**
     * 文档的唯一id
     */
    @Id
    private String id;
    /**
     * 添加索引
     */
    @Indexed
    private String userId;

    private String name;

    private String sex;

    @Field(value = "money_amount")
    private BigDecimal moneyAmount;

    private Double moneyAmount2;

    private Long mongAmountFen;

    private Integer age;

    private Timestamp createTime;

    private Date update_time;

    @Transient
    private Integer isDel;
}
