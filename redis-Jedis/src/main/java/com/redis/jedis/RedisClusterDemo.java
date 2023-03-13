package com.redis.jedis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 演示redis集群操作
 */
public class RedisClusterDemo {
  public static void main(String[] args) {
    // 创建对象
    HostAndPort hostAndPort = new HostAndPort("192.168.200.130", 6381);
    JedisCluster jedisCluster = new JedisCluster(hostAndPort);

    // 进行操作
    jedisCluster.set("b1", "valuel");
    String b1 = jedisCluster.get("b1");
    System.out.println("b1: " + b1);

    jedisCluster.close();
  }
}
