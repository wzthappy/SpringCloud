package cn.itcast.hotel.config;

import cn.itcast.hotel.constants.MqConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
  @Bean // 创建 通配符交换机
  public TopicExchange topicExchange() {
    return new TopicExchange(MqConstants.HOTEL_EXCHANGE, true, false);
  }

  @Bean // 创建队列
  public Queue insertQueue() {
    return new Queue(MqConstants.HOTEL_INSERT_QUEUE, true);
  }
  @Bean
  public Queue deleteQueue() {
    return new Queue(MqConstants.HOTEL_DELETE_QUEUE, true);
  }

  @Bean // 定义 队列和交换机绑定关系
  public Binding insertQueueBinding () {
    return BindingBuilder.bind(insertQueue()).to(topicExchange()).with(MqConstants.HOTEL_INSERT_KEY);
  }
  @Bean
  public Binding deleteQueueBinding () {
    return BindingBuilder.bind(deleteQueue()).to(topicExchange()).with(MqConstants.HOTEL_DELETE_KEY);
  }

}
