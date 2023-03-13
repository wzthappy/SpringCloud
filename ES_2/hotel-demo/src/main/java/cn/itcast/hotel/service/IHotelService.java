package cn.itcast.hotel.service;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.RequestParams;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IHotelService extends IService<Hotel> {
  PageResult search(RequestParams params);

  Map<String, List<String>> filters(RequestParams params);

  List<String> getSuggestions(String prefix);

  void deleteById(Long id);

  void insertById(Long id);
}
