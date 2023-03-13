package com.itheima.rabbitmq.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送消息
 */
public class Producer_PubSub {
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

    // 5. 创建交换机
    String exchangName = "test_fanout";
    channel.exchangeDeclare(exchangName, BuiltinExchangeType.FANOUT, true, false, false, null);

    // 6. 创建队列
    channel.queueDeclare("test_fanout_queue1", true, false, false, null);
    channel.queueDeclare("test_fanout_queue2", true, false, false, null);

    // 7. 绑定队列和交换机
    channel.queueBind("test_fanout_queue1", exchangName, "");
    channel.queueBind("test_fanout_queue2", exchangName, "");

    // 8. 发送消息
    String body = "日志消息: 张三调用了findAll方法...日志级别: info...";
    channel.basicPublish(exchangName, "", null, body.getBytes());

    // 9. 释放资源
    channel.close();
    conn.close();
  }
}
