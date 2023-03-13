package cn.itcast.feign.clients.fallback;

import cn.itcast.feign.clients.UserClient;
import cn.itcast.feign.pojo.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

  @Override
  public UserClient create(final Throwable throwable) {
    // 创建接口的实现类，实现其中的方法，编写失败降级的处理
    return new UserClient() {
      @Override
      public User findById(Long id) {
        // 记录异常信息
        log.error("查询用户异常: ", throwable);
        System.err.println("c---------------");
        // 返回默认的对象
        return new User();
      }
    };
  }
}
