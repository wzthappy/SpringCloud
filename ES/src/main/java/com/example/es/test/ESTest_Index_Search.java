package com.example.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ESTest_Index_Search {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient esClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 查询索引
    GetIndexRequest getIndexRequest = new GetIndexRequest("user");// 查询的索引名称
    GetIndexResponse getIndexResponse = esClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
    // 响应状态
    System.out.println(getIndexResponse.getAliases());
    System.out.println(getIndexResponse.getMappings());
    System.out.println(getIndexResponse.getSettings());

    // 关闭ES客户端
    esClient.close();
  }
}
