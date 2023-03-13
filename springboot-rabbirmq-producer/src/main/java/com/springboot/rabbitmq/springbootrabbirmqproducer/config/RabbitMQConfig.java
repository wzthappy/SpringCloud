package com.springboot.rabbitmq.springbootrabbirmqproducer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  public static final String EXCHANGE_NAME = "boot_topic_exchange";
  public static final String QUEUE_NAME = "boot_queue";

  // 1. 交换机
  @Bean("bootExchange")
  public Exchange bootExchange() {
    return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build(); // 通配符交换机
  }
  @Bean("text_exchange_ttl") // 第二个交换机
  public Exchange exchangeTTL() {
    return ExchangeBuilder.topicExchange("text_exchange_ttl")
        .durable(true).build();
  }
  @Bean("exchange_dlx") // 第3个交换机(死信交换机)
  public Exchange exchangeDLX() {
    return ExchangeBuilder.topicExchange("exchange_dlx") // topic通配符交换机
        .durable(true).build();
  }

  // 2. Queue 队列
  @Bean("bootQueue")
  public Queue bootQueue() {
    return QueueBuilder.durable(QUEUE_NAME).build();
  }
  @Bean("test_queue_ttl") // 第2个队列
  public Queue testQueueTTL() {
    return QueueBuilder.durable("test_queue_ttl") // 要创建的队列名
        .ttl(10000) // 队列中所有的消息存活的时间 是 10秒
        .deadLetterExchange("exchange_dlx")  // 绑定死信交换机
        .deadLetterRoutingKey("dlx.haha")  // 路由key
        .build();  // 构建出这个队列

  }
  @Bean("queue_dlx") // 第3个队列 (死信队列)
  public Queue testQueueDLX() {
    return QueueBuilder.durable("queue_dlx") // 要创建的队列名
        .build();  // 构建出这个队列
  }

  // 3. 队列和交换机绑定关系 Binding
  @Bean
  public Binding bindQueueExchange(@Qualifier("bootQueue") Queue queue,@Qualifier("bootExchange") Exchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
  }
  @Bean
  public Binding bindExchange(@Qualifier("test_queue_ttl") Queue queue, @Qualifier("text_exchange_ttl") Exchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("ttl.#").noargs();
  }

  @Bean // 死信交换机根死信队列绑定
  public Binding bindExchangeDLX(@Qualifier("queue_dlx") Queue queue,@Qualifier("exchange_dlx") Exchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("dlx.#").noargs();
  }
}
