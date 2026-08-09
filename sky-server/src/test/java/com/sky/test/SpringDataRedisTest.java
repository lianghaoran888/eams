package com.sky.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);

        ValueOperations valueOperations = redisTemplate.opsForValue();//获取值操作对象
        HashOperations hashOperations = redisTemplate.opsForHash();//获取哈希操作对象
        ListOperations listOperations = redisTemplate.opsForList();//获取列表操作对象
        SetOperations setOperations = redisTemplate.opsForSet();//获取集合操作对象
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();//获取有序集合操作对象
    }

    @Test
    @DisplayName("测试字符串操作")
    public void testString(){
        // set get setex setnx
        // 插入字符串数据 set
        redisTemplate.opsForValue().set("name", "梁浩然lhr");

        // 获取字符串数据 get
        String name = (String) redisTemplate.opsForValue().get("name");
        System.out.println(name);

        // 插入限时字符串数据 setex
        redisTemplate.opsForValue().set("code", "7777", 2, TimeUnit.MINUTES);

        // 判断键是否存在,如果不存在,则创建字符串 setnx
        redisTemplate.opsForValue().setIfAbsent("code","99999999");// 存在code键,则不创建
    }

    @Test
    @DisplayName("测试哈希操作")
    public void testHash(){
        // hset hget hkeys hvals hdel
        // 获取哈希数据对象
        HashOperations hashOperations = redisTemplate.opsForHash();

        // 插入哈希数据 hset
        hashOperations.put("中国", "beijing", "siheyuan");
        hashOperations.put("中国", "shanghai", "renminguangchang");
        hashOperations.put("中国", "guangzhou", "tianhequ");

        // 获取哈希数据 hget
        String beijing = (String) hashOperations.get("中国", "beijing");
        System.out.println(beijing);

        // 获取哈希所有的键 hkeys
        Set keys = hashOperations.keys("中国");
        System.out.println(keys);

        // 获取哈希所有的值 hvals
        List values = hashOperations.values("中国");
        System.out.println(values);

        // 删除哈希数据 hdel
        hashOperations.delete("中国", "guangzhou");
    }

    @Test
    @DisplayName("测试列表操作")
    public void testList(){
        // lpush rpop lrange llen
        // 获取列表数据对象
        ListOperations listOperations = redisTemplate.opsForList();

        // 插入列表数据 lpush
        listOperations.leftPushAll("mylist", "a","b","c");// 插入a,b,c
        listOperations.leftPush("mylist", "d");// 插入d

        // 获取列表数据 lrange
        List mylist = listOperations.range("mylist", 0, -1);
        System.out.println(mylist);

        // 获取列表长度 llen
        Long size = listOperations.size("mylist");
        System.out.println(size);

        // 删除列表数据 rpop
        listOperations.rightPop("mylist");

        // 再次获取列表长度 llen
        size = listOperations.size("mylist");
        System.out.println(size);
    }

    @Test
    @DisplayName("测试集合操作")
    public void testSet(){
        // sadd smembers srem scard sinter sunion
        // 获取集合数据对象
        SetOperations setOperations = redisTemplate.opsForSet();

        // 插入集合数据 sadd
        setOperations.add("set1", "a","b","c");
        setOperations.add("set2", "b","c","f");

        // 获取集合数据 smembers
        Set set2 = setOperations.members("set2");
        System.out.println(set2);

        // 获取集合长度 scard
        Long size = setOperations.size("set2");
        System.out.println(size);

        // 两集合的交集 sinter
        Set intersect = setOperations.intersect("set1", "set2");
        System.out.println(intersect);

        // 两集合的并集 sunion
        Set union = setOperations.union("set1", "set2");
        System.out.println(union);

        // 删除集合数据 srem
        setOperations.remove("set2", "b");

        // 再次获取集合长度 scard
        size = setOperations.size("set2");
        System.out.println(size);
    }

    @Test
    @DisplayName("测试有序集合操作")
    public void testZSet(){
        // zadd zrange zrem zincrby
        // 获取有序集合数据对象
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        // 插入有序集合数据 zadd
        zSetOperations.add("zset1", "a", 10.8);
        zSetOperations.add("zset1", "b", 7.8);
        zSetOperations.add("zset1", "c", 12.6);

        // 获取有序集合数据 zrange
        Set range = zSetOperations.range("zset1", 0, -1);
        System.out.println(range);

        // 给数据增加分数 zincrby
        zSetOperations.incrementScore("zset1", "b", 5);

        // 删除有序集合数据 zrem
        zSetOperations.remove("zset1", "a");

        // 再次获取有序集合数据 zrange
        range = zSetOperations.range("zset1", 0, -1);
        System.out.println(range);
    }

    @Test
    @DisplayName("测试常用命令")
    public void testCommon(){
        // keys del type exists
        // 获取所有键 keys
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);

        // 查看键是否存在 exists
        Boolean codeExists = redisTemplate.hasKey("code");
        System.out.println("code键是否存在: " + codeExists);
        Boolean nameExists = redisTemplate.hasKey("name");
        System.out.println("name键是否存在: " + nameExists);

        // 查看键的类型 type
        for (Object key : keys) {
            DataType type = redisTemplate.type(key);
            System.out.println(key + "的类型为: " + type);
        }

        // 删除键 del
        redisTemplate.delete("family");

    }
}
