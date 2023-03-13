package com.heima.item.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfig {

  @Bean
  public Cache<Long, Item> itemCache () {
    return Caffeine.newBuilder()
        .initialCapacity(100) // 初始化大小
        .maximumSize(10_000)  // 表示这个缓存最多只能有1万条数据
        .build();
  }

  @Bean
  public Cache<Long, ItemStock> stockCache () {
    return Caffeine.newBuilder()
        .initialCapacity(100) // 初始化大小
        .maximumSize(10_000)  // 表示这个缓存最多只能有1万条数据
        .build();
  }
}
