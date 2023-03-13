package com.heima.item.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import com.heima.item.service.impl.ItemService;
import com.heima.item.service.impl.ItemStockService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisHandIer implements InitializingBean {

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Autowired
  private ItemService itemService;
  @Autowired
  private ItemStockService stockService;

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Override
  public void afterPropertiesSet() throws Exception {
    // 初始化缓存
    // 1. 查询商品信息
    List<Item> itemList = itemService.list();

    // 2. 放入缓存
    for (Item item : itemList) {
      // 2.1 item序列化为JSON
      String json = MAPPER.writeValueAsString(item);
      // 2.2 存入redis
      redisTemplate.opsForValue().set("item:id:" + item.getId(), json);
    }

    // 3. 查询商品库存信息
    List<ItemStock> stockList = stockService.list();

    // 2. 放入缓存
    for (ItemStock stock : stockList) {
      // 2.1 item序列化为JSON
      String json = MAPPER.writeValueAsString(stock);
      // 2.2 存入redis
      redisTemplate.opsForValue().set("item:stock:id:" + stock.getId(), json);
    }
  }

  // 新增 redis
  public void saveItem (Item item) {
    String json = null;
    try {
      json = MAPPER.writeValueAsString(item);
      redisTemplate.opsForValue().set("item:id:" + item.getId(), json);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  // 删除 redis
  public void deleteItemById (Long id) {
    redisTemplate.delete("item:id:" + id);
  }
}
