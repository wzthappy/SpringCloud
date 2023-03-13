package com.itheima.rabbitmq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送消息
 */
public class Producer_HelloWorld {
  public static void main(String[] args) throws IOException, TimeoutException {
    // 1. 创建连接工厂
    ConnectionFactory factory = new ConnectionFactory();

    // 2. 设置参数
    factory.setHost("192.168.200.130"); // ip 地址    (默认为localhost)
    factory.setPort(5672); // 端口  (默认也是5672)
    factory.setVirtualHost("/wztcast"); // 虚拟机  (默认为/)
    factory.setUsername("wzt"); // 用户名  (默认为guest)
    factory.setPassword("wzt"); // 密码 (默认值为guest)

    // 3. 获取连接 Connection
    Connection conn = factory.newConnection();

    // 4. 创建Channel
    Channel channel = conn.createChannel();

    // 5. 创建队列Queue
    channel.queueDeclare("hello_world", true, false, false, null);

    // 6. 发送消息
    String body = "hello rabbitmq~~~~";
    channel.basicPublish("", "hello_world", null, body.getBytes());

    // 7. 释放资源
    channel.close();
    conn.close();
  }
}
