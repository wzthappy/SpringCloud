package com.example.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;

public class ESTest_Doc_Get {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient esClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 查询数据
    GetRequest getRequest = new GetRequest();
    getRequest.index("user").id("1001");
    GetResponse response = esClient.get(getRequest, RequestOptions.DEFAULT);

    System.out.println(response.getSourceAsString());

    // 关闭ES客户端
    esClient.close();
  }
}
