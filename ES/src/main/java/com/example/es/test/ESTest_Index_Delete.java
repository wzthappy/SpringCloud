package com.example.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

import java.io.IOException;

public class ESTest_Index_Delete {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient esClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 删除索引
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("user");// 删除索引
    AcknowledgedResponse delete = esClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    // 响应状态
    boolean acknowledged = delete.isAcknowledged();
    System.out.println(acknowledged);

    // 关闭ES客户端
    esClient.close();
  }
}
