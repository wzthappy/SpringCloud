package com.itheima.rabbitmq.producer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_Routing2 {
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


    // 5. 接收消息
    Consumer consumer = new DefaultConsumer(channel) {
      // 回调方法，当收到消息后，会自动执行该方法
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//        System.out.println("consumerTag: " + consumerTag);
//        System.out.println("Envelope: " + envelope.getExchange());
//        System.out.println("EnvelopeKey: " + envelope.getRoutingKey());
//        System.out.println("properties: " + properties);
        System.out.println("body: " + new String(body));
        System.out.println("将日志打印到控制台....");
      }
    };
    channel.basicConsume("test_direct_queue2", true, consumer);

    // 消费者不要关闭资源
  }
}
