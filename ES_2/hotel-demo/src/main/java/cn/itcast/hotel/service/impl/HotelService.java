package cn.itcast.hotel.service.impl;

import cn.itcast.hotel.mapper.HotelMapper;
import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.RequestParams;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
  @Autowired
  private RestHighLevelClient client;

  @Override
  public PageResult search(RequestParams params) {
    try {
      // 1. 准备request
      SearchRequest request = new SearchRequest("hotel");
      // 2. 准备DSL
      // 2. 1 query
      buildBasicQuery(params, request);

      // 2.2 分页
      Integer page = params.getPage();
      Integer size = params.getSize();
      request.source().from((page - 1) * size).size(size);
      // 2.3 地理排序
      String location = params.getLocation();
      if (location != null && location.equals("")) {
        request.source().sort(SortBuilders
            .geoDistanceSort("location", new GeoPoint(location))
            .order(SortOrder.ASC)
            .unit(DistanceUnit.KILOMETERS));
      }

      // 3. 发送请求，得到响应
      SearchResponse search = client.search(request, RequestOptions.DEFAULT);

      // 3. 解析响应
      return handleResponse(search);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void buildBasicQuery(RequestParams params, SearchRequest request) {
    // 构建BooleanQuery
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    // 关键字搜索
    String key = params.getKey();
    if (key == null || "".equals(key)) {
      boolQuery.must(QueryBuilders.matchAllQuery());
    } else {
      boolQuery.must(QueryBuilders.matchQuery("all", key));
    }
    // 条件过滤
    // 城市条件
    if (params.getCity() != null && !params.getCity().equals("")) {
      boolQuery.filter(QueryBuilders.termQuery("city", params.getCity()));
    }
    // 品牌条件
    if (params.getBrand() != null && !params.getBrand().equals("")) {
      boolQuery.filter(QueryBuilders.termQuery("brand", params.getBrand()));
    }
    // 星级条件
    if (params.getStarName() != null && !params.getStarName().equals("")) {
      boolQuery.filter(QueryBuilders.termQuery("starName", params.getStarName()));
    }
    // 价格
    if (params.getMinPrice() != null && params.getMaxPrice() != null) {
      boolQuery.filter(QueryBuilders.rangeQuery("proce")
          .gte(params.getMinPrice()).lte(params.getMaxPrice()));
    }

    // 2. 算分控制
    FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders
        .functionScoreQuery(
            // 原始查询，相关算分的查询
            boolQuery,
            // function score的数组
            new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                // 其中一个function score 元素
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                    // 过滤条件
                    QueryBuilders.termQuery("isAD", true),
                    // 算分函数
                    ScoreFunctionBuilders.weightFactorFunction(10)
                )
            });

    request.source().query(functionScoreQuery);
  }


  @Override
  public Map<String, List<String>> filters(RequestParams params) {
    try {
      Map<String, List<String>> result = new HashMap<>();
      // 1. 准备Request
      SearchRequest request = new SearchRequest("hotel");
      // 2. 准备DSL
      // 2.0 查询信息，限定聚合范围
      buildBasicQuery(params, request);
      // 2.1 聚合
      request.source().size(0);
      buidAggregation(request);

      // 3. 发送请求
      SearchResponse response = client.search(request, RequestOptions.DEFAULT);

      // 4. 解析结果
      // 4.1 根据聚合名称获取结果
      List<String> buandList = getAggByName(response, "brandAgg");
      List<String> cityList = getAggByName(response, "cityAgg");
      List<String> starAgg = getAggByName(response, "starAgg");

      // 4.2 放入map
      result.put("品牌", buandList);
      result.put("城市", cityList);
      result.put("星级", starAgg);

      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<String> getSuggestions(String prefix) {
    try {
      // 1. 准备request
      SearchRequest request = new SearchRequest("hotel");

      // 2. 请求参数
      request.source().suggest(new SuggestBuilder()
          .addSuggestion("suggestions", // 自定义 自动补全的名称
              SuggestBuilders.completionSuggestion("suggestion") // 补全字段的名称
                  .prefix(prefix)  // 查询的关键字(前缀)
                  .skipDuplicates(true) // 去重
                  .size(10) // 长度
          ));

      // 3. 发送请求
      SearchResponse response = client.search(request, RequestOptions.DEFAULT);

      // 4. 解析
      CompletionSuggestion suggestion = response.getSuggest().getSuggestion("suggestions");
      List<CompletionSuggestion.Entry.Option> options = suggestion.getOptions();
      List<String> list = new ArrayList<>(options.size());
      for (CompletionSuggestion.Entry.Option option : options) {
        String text = option.getText().string();
        list.add(text);
      }
      return list;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override // 根据id删除文档
  public void deleteById(Long id) {
    try {
      // 1. 准备request
      DeleteRequest request = new DeleteRequest("hotel", id.toString());

      // 2. 准备发送请求
      client.delete(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override // 根据id新增文档
  public void insertById(Long id) {
    try {
      // 0. 根据id查询酒店的数据
      Hotel hotel = getById(id);
      // 装换为文档类型
      HotelDoc hotelDoc = new HotelDoc(hotel);

      // 1. 准备request
      IndexRequest request = new IndexRequest("hotel").id(hotelDoc.getId().toString());
      // 2. 准备DSL
      request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
      // 3. 发送请求
      client.index(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> getAggByName(SearchResponse response, String aggName) {
    // 4. 解析结果
    Terms terms = response.getAggregations().get(aggName);
    List<? extends Terms.Bucket> buckets = terms.getBuckets();
    // 4.1 遍历
    List<String> buandList = new ArrayList<>();
    for (Terms.Bucket bucket : buckets) {
      String key = bucket.getKeyAsString();
      buandList.add(key);
    }
    return buandList;
  }

  private void buidAggregation(SearchRequest request) {
    request.source().aggregation(AggregationBuilders
        .terms("brandAgg")
        .field("brand").size(100)
    );
    request.source().aggregation(AggregationBuilders
        .terms("cityAgg")
        .field("city").size(100)
    );
    request.source().aggregation(AggregationBuilders
        .terms("starAgg")
        .field("starName").size(100)
    );
  }

  // 4. 解析
  private PageResult handleResponse(SearchResponse response) {
    List<HotelDoc> hotels = new ArrayList<>();

    // 4.1 解析结果
    SearchHits searchHits = response.getHits();
    // 4.2 查询的总条数
    long total = searchHits.getTotalHits().value;
    // 4.3 查询结果数组
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
      // 3.4 得到单个结果
      String json = hit.getSourceAsString();
      // 反序列化
      HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
      // 获取排序值
      Object[] sortValues = hit.getSortValues();
      if (sortValues.length > 0) {
        Object sortValue = sortValues[0];
        hotelDoc.setDistance(sortValue);
      }
      hotels.add(hotelDoc);
    }
    // 4.4 封装返回
    return new PageResult(total, hotels);
  }

}
