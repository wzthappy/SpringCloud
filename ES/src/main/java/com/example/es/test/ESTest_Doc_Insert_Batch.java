package com.example.es.test;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.Arrays;

public class ESTest_Doc_Insert_Batch {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient esClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 批量插入数据
    BulkRequest bulkRequest = new BulkRequest();

    bulkRequest.add(new IndexRequest().index("user").id("1001").source(XContentType.JSON, "name", "zhang"));
    bulkRequest.add(new IndexRequest().index("user").id("1002").source(JSON.toJSONString(new User("kaikai", "男", 14)), XContentType.JSON));
    bulkRequest.add(new IndexRequest().index("user").id("1003").source(JSON.toJSONString(new User("2134", "男", 13)) ,XContentType.JSON));
    bulkRequest.add(new IndexRequest().index("user").id("1004").source(JSON.toJSONString(new User("lllll", "男", 12)) ,XContentType.JSON));
    bulkRequest.add(new IndexRequest().index("user").id("1005").source(XContentType.JSON, "name", "22222", "sex", "女", "age", 20));
    bulkRequest.add(new IndexRequest().index("user").id("1006").source(XContentType.JSON, "name", "314"));

    BulkResponse bulk = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    System.out.println(bulk.getTook());
    System.out.println(Arrays.toString(bulk.getItems()));

    // 关闭ES客户端
    esClient.close();
  }
}
