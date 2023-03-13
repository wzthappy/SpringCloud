package cn.itcast.hotel.web;

import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.RequestParams;

import cn.itcast.hotel.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author baibao
 * @date 2022/06/17/15:58
 */
@RestController
@RequestMapping("/hotel")
public class HotelController {

  @Autowired
  private IHotelService hotelService;

  // 搜索酒店数据
  @PostMapping("/list")
  public PageResult search(@RequestBody RequestParams params){
    return hotelService.search(params);
  }

  @PostMapping("/filters")
  public Map<String, List<String>> getFilters(@RequestBody RequestParams params){
    return hotelService.filters(params);
  }

  @GetMapping("/suggestion")
  public List<String> getSuggestions (@RequestParam("key") String prefix) {
    return hotelService.getSuggestions(prefix);
  }
}
