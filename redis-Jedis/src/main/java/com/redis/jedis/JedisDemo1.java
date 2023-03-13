package com.redis.jedis;

import org.testng.annotations.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class JedisDemo1 {
  static Jedis jedis;
  static {
    // 1. 创建 Jedis 对象
    jedis = new Jedis("192.168.200.130", 6379);
    jedis.auth("18729231365wzt");
  }

  @Test // 测试 是否连接成功
  public void demo() {
    String ping = jedis.ping();
    System.out.println(ping);
  }

  @Test // 操作key
  public void demo1() {
    // 添加
    jedis.set("name",  "lucy");
    // 获取
    String name = jedis.get("name");
    System.out.println(name);
    System.out.println(jedis.ttl("name")); // 存活时间

    // 设置多个key-value
    jedis.mset("k1", "v1", "k2", "v2");
    List<String> mget = jedis.mget("k1", "k2");
    mget.forEach(System.out::println);


    System.out.println("------- 打印所有的key -------");
    Set<String> keys = jedis.keys("*");
    for (String key : keys) {
      System.out.println(key);
    }
  }

  @Test // 操作 list
  public void demo2() {
    jedis.lpush("key1", "lucy", "mary", "jack");

    List<String> values = jedis.lrange("key1", 0, -1); // 查询
    System.out.println(values);
  }

  @Test // 操作 set
  public void demo3() {
    jedis.sadd("names", "lucy", "jack");

    Set<String> name = jedis.smembers("names"); // 查询
    System.out.println(name);
  }

  @Test // 操作 hash
  public void demo4 () {
    HashMap<String, String> s = new HashMap<>();
    s.put("zhang", "vvvzhang");
    s.put("中国", "vvvd2");
    jedis.hset("users", s);

    List<String> users = jedis.hvals("users");
    System.out.println(users);
  }

  @Test // 操作 zset
  public void demo5 () {
    jedis.zadd("ch", 100, "上海");
    jedis.zadd("ch", 120, "堵塞");
    Set<String> ch = jedis.zrange("ch", 0, -1);
    System.out.println(ch);
  }
}
