package com.redis.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;

public class PhoneCode {
  public static void main(String[] args) {
    // 模拟验证码的发送       假的
    verifyCode("187");

    getRedisCode("187", "490966");
  }

  // 1. 生成6位数字验证码
  public static String getCode() {
    Random random = new Random();
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 6; i++) {
      code.append(random.nextInt(10));
    }
    return String.valueOf(code);
  }

  // 2. 每个手机每天只能发送三次，验证码放到redis中，设置过期时间 120秒
  public static void verifyCode(String phone) {
    // 连接redis
    Jedis jedis = new Jedis("192.168.200.130", 6379);
    jedis.auth("18729231365wzt");
    // 拼接key
    // 手机发送次数
    String countKey = "VerifyCode" + phone + ":count";
    // 验证码key
    String codeKey = "VerifyCode" + phone + ":code";
    // 每个手机每天只能发送三次
    String count = jedis.get(countKey);
    if (count == null) {
      // 没有发送次数，第一次发送
      // 设置发送次数是1
      jedis.setex(countKey, 60 * 60 * 24, "1");
    } else if (Integer.parseInt(count) <= 2) {
      // 发送次数+1
      jedis.incr(countKey);
    } else if (Integer.parseInt(count) > 2) {
      // 发送三次，不能在发送
      System.out.println("今天发送次数已经超过三次");
      jedis.close(); // 关闭流
      return;
    }

    // 发送的验证码放到redis里面
    String vcode = getCode();
    jedis.setex(codeKey, 120, vcode);
    jedis.close();
  }

  // 3. 验证码校验
  public static void getRedisCode(String phone, String code) {
    // 连接redis
    Jedis jedis = new Jedis("192.168.200.130", 6379);
    jedis.auth("18729231365wzt");
    // 从redis获取验证码、 验证码key
    String codeKey = "VerifyCode" + phone + ":code";
    String redisCode = jedis.get(codeKey);
    // 判断
    if (redisCode.equals(code)) {
      System.out.println("成功");
    } else {
      System.out.println("失败");
    }
    jedis.close();
  }
}
