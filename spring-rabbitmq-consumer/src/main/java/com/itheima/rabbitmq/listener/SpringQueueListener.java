package com.itheima.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class SpringQueueListener implements MessageListener {
  @Override
  public void onMessage(Message message) {
    System.out.println(new String(message.getBody()));
  }

}
