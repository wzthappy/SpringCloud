package com.example.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ESTest_Client {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 关闭ES客户端
    restHighLevelClient.close();
  }
}
