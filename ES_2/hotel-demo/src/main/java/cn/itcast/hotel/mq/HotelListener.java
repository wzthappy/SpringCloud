package cn.itcast.hotel.mq;

import cn.itcast.hotel.constants.MqConstants;
import cn.itcast.hotel.service.IHotelService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HotelListener {
  @Autowired
  private IHotelService iHotelService;

  // 监听酒店新增或修改的业务    id: 酒店id
  @RabbitListener(queues = MqConstants.HOTEL_INSERT_QUEUE)
  public void listenHotelInsertOrUpdate(Long id) {
    iHotelService.insertById(id);
  }

  // 监听酒店删除的业务    id: 酒店id
  @RabbitListener(queues = MqConstants.HOTEL_DELETE_QUEUE)
  public void listenHotelDelete(Long id) {
    iHotelService.deleteById(id);
  }
}
