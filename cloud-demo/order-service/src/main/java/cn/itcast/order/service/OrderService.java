package cn.itcast.order.service;

import cn.itcast.feign.clients.UserClient;
import cn.itcast.feign.pojo.User;
import cn.itcast.order.mapper.OrderMapper;
import cn.itcast.order.pojo.Order;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  @Autowired
  private OrderMapper orderMapper;

  @Autowired
  @Lazy
  private UserClient userClient;


  public Order queryOrderById(Long orderId) {
    // 1.查询订单
    Order order = orderMapper.findById(orderId);
    // 2. 利用 Feign 远程调用
    User user = userClient.findById(order.getUserId());
    // 3. 封装user到order中
    order.setUser(user);
    // 4.返回
    return order;
  }


  // 添加为sentinel资源
  @SentinelResource("goods")
  public void queryGoods() {
    System.err.println("查询商品");
  }

/*    @Autowired
    RestTemplate restTemplate;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2. 利用RestTemplate发起http请求，查询用户
        // 2.1 url路径
        String url = "http://userservice/user/" + order.getUserId();
        // 2.2 发送http请求，实现远程调用
        User user = restTemplate.getForObject(url, User.class);
        // 3. 封装user到order中
        order.setUser(user);
        // 4.返回
        return order;
    }*/


}
