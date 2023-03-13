//package cn.itcast.order.Config;
//
//import com.alibaba.cloud.nacos.ribbon.NacosRule;
//import com.netflix.loadbalancer.IRule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class HttpConfiguration {
//
////  @Bean // 创建RestTemplate并注入Spring容器
////  @LoadBalanced // 负载均衡
////  public RestTemplate restTemplate() {
////    return new RestTemplate();
////  }
//
//  @Bean   // 发送请求会优先找与自己相同的集群
//  public IRule nacosRule() {
//    return new NacosRule(); // 负载均衡规则
//  }
//
//}
