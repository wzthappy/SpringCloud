package com.springboot.redis.redisspringboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
public class SecKillController {

  @Autowired
  private RedisTemplate redisTemplate;

  @GetMapping("/doseckill")
  @ResponseBody
//  , String seckill_btn
  public Boolean p (String prodid) {
    String userid = new Random().nextInt(5000) + "";
    Boolean isSuccess = doSeckill(userid, prodid);
    return isSuccess;
  }

  // 秒杀的过程
  private Boolean doSeckill(String uid, String prodid) {
    // 1. uid 和 prodid非空判断
    if (uid == null || prodid ==  null) {
      return false;
    }

    // 2. 连接redis
//     redisTemplate

    // 3. 拼接key
    // 3.1 库存key
    String kcKey = "sk" + prodid + ":qt";

    // 3.2 秒杀成功用户key
    String userKey = "sk" + prodid + ":user";

    // 监视库存
//    redisTemplate.watch(kcKey);

    // 4. 获取库存，如果库存null，秒杀还没有开始
    String kc = String.valueOf(redisTemplate.opsForValue().get(kcKey));
    if (kc == null) {
      System.out.println("秒杀还没有开始，请等待");
      return false;
    }

    // 5. 判断用户是否重复秒杀操作
    if (redisTemplate.opsForSet().isMember(userKey, uid)) {
      System.out.println("你已经秒杀成功了，不能重复秒杀");
//      return false;
//    }
//
//    // 6. 判断如果商品数量，库存数量小于1，秒杀结束
//    if (Integer.parseInt(kc) <= 0) {
//      System.out.println("秒杀已经结束了");
//      return false;
//    }
//
//    // 7. 秒杀的过程
//    //2.使用事务
////    redisTemplate.multi();

    //3.组队操作
    redisTemplate.boundValueOps(kcKey).decrement(1);

    redisTemplate.opsForSet().add(userKey, uid);

    //4.执行
//    List<Object> exec = redisTemplate.exec();
//    if(exec == null || exec.size()==0) {
//      System.out.println("秒杀失败了....");
//      return false;
//    }
    System.out.println("秒杀成功了..");

    return true;
  }
}
