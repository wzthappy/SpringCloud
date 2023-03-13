package com.heima.item.canal;

import com.github.benmanes.caffeine.cache.Cache;
import com.heima.item.config.RedisHandIer;
import com.heima.item.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

@CanalTable("tb_item")
@Component
public class ItemHandier implements EntryHandler<Item> {

  @Autowired
  private RedisHandIer redisHandIer;

  @Autowired
  private Cache<Long, Item> itemCache;

  @Override
  public void insert(Item item) {
    // 表中新增了数据
    // 写数据到JVM进程缓存
    itemCache.put(item.getId(), item);
    // 写数据到redis
    redisHandIer.saveItem(item);
  }

  @Override
  public void update(Item before, Item after) {
    // 表中数据更新了
    // 写数据到JVM进程缓存
    itemCache.put(after.getId(), after);
    // 写数据到redis
    redisHandIer.saveItem(after);
  }

  @Override
  public void delete(Item item) {
    // 表中删除了数据
    // 删除数据 JVM进程缓存
    itemCache.invalidate(item.getId());
    // 删除数据 redis
    redisHandIer.deleteItemById(item.getId());
  }
}
