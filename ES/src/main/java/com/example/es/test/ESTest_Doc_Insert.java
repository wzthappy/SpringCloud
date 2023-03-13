package com.example.es.test;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.lang.runtime.ObjectMethods;

public class ESTest_Doc_Insert {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient esClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 插入数据
    IndexRequest request = new IndexRequest();
    request.index("user").id("1001");
    User user = new User("zhangsan", "男", 30);
    // 向ES插入数据，必须将数据转换为JSON格式
    String userJSON = JSON.toJSONString(user);
    request.source(userJSON, XContentType.JSON);

    IndexResponse index = esClient.index(request, RequestOptions.DEFAULT);

    System.out.println(index.getResult());

    // 关闭ES客户端
    esClient.close();
  }
}
