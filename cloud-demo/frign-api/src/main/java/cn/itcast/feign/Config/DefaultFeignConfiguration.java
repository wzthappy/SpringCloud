package cn.itcast.feign.Config;

import cn.itcast.feign.clients.fallback.UserClientFallbackFactory;
import org.springframework.context.annotation.Bean;
public class DefaultFeignConfiguration {
//    @Bean
//    public Logger.Level logLevel(){
//        return Logger.Level.BASIC;
//    }

    @Bean
    public UserClientFallbackFactory userClientFallbackFactory(){
        return new UserClientFallbackFactory();
    }
}
