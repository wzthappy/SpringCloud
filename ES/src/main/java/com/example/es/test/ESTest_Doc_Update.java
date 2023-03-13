package com.example.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;

public class ESTest_Doc_Update {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient esClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 修改数据
    UpdateRequest updateRequest = new UpdateRequest();
    updateRequest.index("user").id("1001");
    updateRequest.doc(XContentType.JSON, "sex", "女");

    UpdateResponse update = esClient.update(updateRequest, RequestOptions.DEFAULT);

    System.out.println(update.getResult());

    // 关闭ES客户端
    esClient.close();
  }
}
