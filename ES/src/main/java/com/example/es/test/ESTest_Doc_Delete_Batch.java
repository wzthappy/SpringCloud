package com.example.es.test;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.Arrays;

public class ESTest_Doc_Delete_Batch {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient esClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 批量删除数据
    BulkRequest bulkRequest = new BulkRequest();

    bulkRequest.add(new DeleteRequest().index("user").id("1001"));
    bulkRequest.add(new DeleteRequest().index("user").id("1002"));
    bulkRequest.add(new DeleteRequest().index("user").id("1003"));

    BulkResponse bulk = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    System.out.println(bulk.getTook());
    System.out.println(Arrays.toString(bulk.getItems()));

    // 关闭ES客户端
    esClient.close();
  }
}
