一、MongoDB简介：
MongoDB介于关系数据库和非关系数据库之间的开源产品，是最接近于关系型数据库的NoSQL数据库。

用于存储非结构化数据。即数据结构不规则或不完整，没有预定义的数据模型，不方便用数据库二维逻辑表来表现的数据。

SQL中的绝大多数操作有对应的方式来实现。

在轻量级JSON基础之上进行了扩展，即称为BSON的方式来描述其无结构化的数据类型。

二、SQL与MongoDB对比及映射：
逻辑术语对象：

SQL Terms/Concepts                      MongoDB Terms/Concepts
-------------------                     -----------------------------------
database                                  database
table                                     collection
row                                       document or BSON document
column                                    field
index                                     index
table joins                               embedded documents and linking
primary key(指定一个唯一列或复合列)         primary key(由_id自动生成)
aggregation (e.g. group by)               aggregation pipeline


文档：可以理解为一个文本文件，不过这个文本文件有固定的格式，即为使用BSON的有序键值对。 MongoDB 的文档可以设置为使用不同的字段， 并且相同的字段可以使用不同同的数据类型 文档中的值不仅可以是在双引号里面的字符串，还可以是其他几种数据类型（甚至可以是整个嵌入的文档)。 MongoDB区分类型和大小写。 MongoDB的文档不能有重复的键。 文档的键是字符串。

集合：多个文档组成一个集合，相当于关系型数据库的表。通常包括常规集合以及定长集合 集合存在于数据库中，无固定模式，即使用动态模式。 也就是说集合不要求每一个文档使用相同的数据类型以及列 上述特性及成为free-schema，但通常还是建议将相关类型的文档组织或存放到一个集合里边。 在上面的图示中，即可以看到都多个不同的文档组成了一个集合。

数据库：一个mongodb实例可以包含多个数据库 一个数据库可以包含多个集合 一个集合可以包含多个文档。

表级别对照:

SQL语句                                        mongoDB java script shell
---------------------------------------        --------------------------------------------------
CREATE TABLE users (                           db.users.insert( {   
    id MEDIUMINT NOT NULL                          user_id: "abc123",
        AUTO_INCREMENT,                            age: 55,         
    user_id Varchar(30),                           status: "A"      
    age Number,                                 } )                 
    status char(1),                            //也可以使用下面的方式来创建集合，不过没有结构，即free-schema
    PRIMARY KEY (id)                           db.createCollection("users")
)
 
//表修改(增加列)
---------------------------------------        --------------------------------------------------
ALTER TABLE users                              db.users.update(                       
ADD join_date DATETIME                             { },                               
                                                   { $set: { join_date: new Date() } },
                                                   { multi: true }                    
                                               ) //由于集合无模式，可以直接通过update $set修改器来增加键 
 
//表修改(删除列)             
---------------------------------------        --------------------------------------------------
ALTER TABLE users                              db.users.update(                 
DROP COLUMN join_date                              { },                                                                    
                                                   { $unset: { join_date: "" } },
                                                   { multi: true }              
                                               ) //同表增加列，不过此时使用unset修改器
 
//创建索引 
---------------------------------------        --------------------------------------------------
CREATE INDEX idx_user_id_asc                   db.users.createIndex( { user_id: 1 } )
ON users(user_id)                     
 
//创建索引(多列倒序)
---------------------------------------        --------------------------------------------------
CREATE INDEX                                   db.users.createIndex( { user_id: 1, age: -1 } )
       idx_user_id_asc_age_desc                                             
ON users(user_id, age DESC)
记录插入对照:

SQL语句                                        mongoDB java script shell
---------------------------------------        --------------------------------------------------    
INSERT INTO users(user_id,                     db.users.insert(                              
                  age,                            { user_id: "bcd001", age: 45, status: "A" }
                  status)                      )                                            
VALUES ("bcd001",
        45,
        "A")
记录查询对照:

SQL语句                                        mongoDB java script shell
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find()
FROM users
 
---------------------------------------        -------------------------------------------------- 
SELECT id,                                     db.users.find(                
       user_id,                                    { },                      
       status                                      { user_id: 1, status: 1 } 
FROM users                                     )                             
 
---------------------------------------        -------------------------------------------------- 
SELECT user_id, status                         db.users.find(                          
FROM users                                         { },                            
                                                   { user_id: 1, status: 1, _id: 0 }
                                               )                                   
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find(       
FROM users                                         { status: "A" }  
WHERE status = "A"                             )                    
 
---------------------------------------        -------------------------------------------------- 
SELECT user_id, status                         db.users.find(                       
FROM users                                         { status: "A" },                 
WHERE status = "A"                                 { user_id: 1, status: 1, _id: 0 }
                                               )                                    
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find(                
FROM users                                         { status: { $ne: "A" } }  
WHERE status != "A"                            )                             
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find(      
FROM users                                         { status: "A",  
WHERE status = "A"                                   age: 50 }     
AND age = 50                                   )                   
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find(               
FROM users                                         { $or: [ { status: "A" } ,
WHERE status = "A"                                          { age: 50 } ] } 
OR age = 50                                    )                            
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find(          
FROM users                                         { age: { $gt: 25 } }
WHERE age > 25                                 )                       
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find(         
FROM users                                        { age: { $lt: 25 } }
WHERE age < 25                                 )                      
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find(                  
FROM users                                        { age: { $gt: 25, $lte: 50 } }
WHERE age > 25                                 )                               
AND   age <= 50
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find( { user_id: /bc/ } )
FROM users
WHERE user_id like "%bc%"
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find( { user_id: /^bc/ } )  
FROM users
WHERE user_id like "bc%"
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find( { status: "A" } ).sort( { user_id: 1 } ) 
FROM users
WHERE status = "A"
ORDER BY user_id ASC
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find( { status: "A" } ).sort( { user_id: -1 } )
FROM users
WHERE status = "A"
ORDER BY user_id DESC
 
---------------------------------------        -------------------------------------------------- 
SELECT COUNT(*)                                db.users.count()        
FROM users                                     or  db.users.find().count()                   
 
---------------------------------------        --------------------------------------------------                                                                                   
SELECT COUNT(user_id)                          db.users.count( { user_id: { $exists: true } } )          
FROM users                                     or db.users.find( { user_id: { $exists: true } } ).count()                                                        
 
---------------------------------------        -------------------------------------------------- 
SELECT COUNT(*)                                db.users.count( { age: { $gt: 30 } } )      
FROM users                                     or  db.users.find( { age: { $gt: 30 } } ).count()                                        
WHERE age > 30                                                                             
 
---------------------------------------        -------------------------------------------------- 
SELECT DISTINCT(status)                        db.users.distinct( "status" )   
FROM users
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.findOne()      
FROM users                                     or db.users.find().limit(1)                     
LIMIT 1                                                                
 
---------------------------------------        -------------------------------------------------- 
SELECT *                                       db.users.find().limit(5).skip(10)
FROM users
LIMIT 5
SKIP 10
 
---------------------------------------        -------------------------------------------------- 
EXPLAIN SELECT *                               db.users.find( { status: "A" } ).explain()   
FROM users
WHERE status = "A"
记录删除对照:

SQL语句                                        mongoDB java script shell
---------------------------------------        --------------------------------------------------
DELETE FROM users                              db.users.remove( { status: "D" } )
WHERE status = "D"
 
---------------------------------------        --------------------------------------------------
DELETE FROM users                              db.users.remove({})
其他用法对照：

SQL语句                                        mongoDB java script shell
---------------------------------------        --------------------------------------------------
replace into users(user_id,age)                db.users.save({"_id":"5ffc36ffbb86ae3a3b789769","name":"张三","age":18,"sex":"男"});
values("0001","11");                      
 
---------------------------------------        --------------------------------------------------
select sex, count(*) from                      db.users.aggregate([{$group:{_id : "$sex", num : {$sum : 1}}}])
users group by sex;
三、Spring Boot使用MongoDB：
引入starter：

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
配置数据源：

spring:
  data:
    mongodb:
      uri: mongodb://121.4.119.156:27017/test
编写实体类，spring-data-mongodb中的实体映射是通过MongoMappingConverter这个类实现的。它可以通过注释把java类转换为mongodb的文档。

/**
 * 声明为文档
 */
@Document
@Data
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
 
    /**
     * 忽略该字段
     */
    @Transient
    private Integer isDel;
}


CRUD:
方式一：

//声明接口继承MongoRepository，此时就可以使用接口已有的方法，还可以定义如下(按规则)自定义方法，方法会有springboot实现。
@Repository("userRepository")
public interface UserRepository extends MongoRepository<User, String> {
 
    /**
     * 根据用户名查询
     * @param name
     * @return
     */
    User findByName(String name);
}
方式二：

@Service
public class MongoOperateService {
 
    @Resource
    private MongoTemplate mongotemplate;
 
    public void testQuery() {
        Query query = new Query(Criteria.where("userId").is("001"));
        User user = mongotemplate.findOne(query, User.class);
    }
}
四、示例demo地址：
