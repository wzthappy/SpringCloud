package com.example.es.test;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;

public class ESTest_Doc_Delete {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient esClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 删除数据
    DeleteRequest deleteRequest = new DeleteRequest();
    deleteRequest.index("user").id("1001");
    DeleteResponse delete = esClient.delete(deleteRequest, RequestOptions.DEFAULT);

    System.out.println(delete.toString());

    // 关闭ES客户端
    esClient.close();
  }
}
