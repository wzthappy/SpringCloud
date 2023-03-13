package com.springboot.redis.redisspringboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Controller
@RequestMapping("/redisTest")
public class RedisTestController {

  @Autowired
  private RedisTemplate redisTemplate;

  @GetMapping("/testLock")
  public void testLock() {
    // 1. 获取锁 setne
    String uuid = UUID.randomUUID().toString();
    Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,
        3, TimeUnit.SECONDS);
    // 2. 获取锁成功、查询num的值
    if (lock) {
      Object value = redisTemplate.opsForValue().get("num");
      // 2.1 判断num为空return
      if (StringUtils.isEmpty(value)) {
        return;
      }
      // 2.2 有值就转换成int
      int num = Integer.parseInt(value + "");
      // 2.3 把redis的num加1
      redisTemplate.opsForValue().set("num", ++num);

      // 使用lua脚本来锁
      // 定义lua脚本
      String script = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del', EKYS[1]) else return 0 end";

      // 使用redis执行lua执行
      DefaultRedisScript<Lock> redisScript = new DefaultRedisScript<>();
      redisScript.setScriptText(script);

      redisScript.setResultType(Lock.class);

      redisTemplate.execute(redisScript, Arrays.asList(lock), uuid);

//       2.4 释放锁 del
//       判断比较uuid值是否一样
//      String lockUuid = (String) redisTemplate.opsForValue().get("lock");
//      if (lockUuid.equals(uuid)) {
//        redisTemplate.delete("lock");
//      }
    } else {
      // 3. 获取锁失败、每隔0.1秒在获取
      try {
        Thread.sleep(100);
        testLock();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @GetMapping
  @ResponseBody
  public String testRedis() {
    // 设置值到redis
    redisTemplate.opsForValue().set("name", "lucy");
    // 从redis获取值
    String name = (String) redisTemplate.opsForValue().get("name");
    return name;
  }
}
