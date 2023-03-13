package com.springboot.rabbitmq.springbootrabbirmqconsumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQListener {
  // consumer 限流机制
  @RabbitListener(queues = "test_queue_ttl") // 队列
  public void ListenerQueue2(Message message, Channel channel) throws IOException, InterruptedException {
    long deliveryTag = message.getMessageProperties().getDeliveryTag();
    try {
      // 1. 接收转换消息
      System.out.println(new String(message.getBody()));

      // 2. 处理业务逻辑
      System.out.println("处理业务逻辑....");
      int a = 3 / 0;

      // 3. 手动签收
      channel.basicAck(deliveryTag, true);
    } catch (Exception e) {
      // 4. 拒绝签收                           第三个参数为true表式消息重回队列
      channel.basicNack(deliveryTag, true, false);
    }
  }

  // consumer 限流机制
  @RabbitListener(queues = "boot_queue") // 队列
  public void ListenerQueue(Message message, Channel channel) throws IOException, InterruptedException {
    long deliveryTag = message.getMessageProperties().getDeliveryTag();
    try {
      // 1. 接收转换消息
      System.out.println(new String(message.getBody()));

      // 2. 处理业务逻辑
      System.out.println("处理业务逻辑....");

      // 3. 手动签收
      channel.basicAck(deliveryTag, true);
    } catch (Exception e) {
      // 4. 拒绝签收                           第三个参数为true表式消息重回队列
      channel.basicNack(deliveryTag, true, false);
    }
  }
}
