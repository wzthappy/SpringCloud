package com.example.es.test;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.Arrays;

public class ESTest_Doc_Query {
  public static void main(String[] args) throws IOException {
    // 创建ES客户端
    RestHighLevelClient esClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
    );

    // 1. 查询索引中全部的数据
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//    SearchHits hits = search.getHits();
//
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }
//
    // 2. 条件查询
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("age", 12)));
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//    SearchHits hits = search.getHits();
//
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }

    // 3. 分页查询
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
//    // (当前页面 - 1) * 每页显示条数
//    query.from(2);
//    query.size(2);
//    request.source(query);
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//    SearchHits hits = search.getHits();
//
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }

    // 4. 查询排序
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
//    query.sort("age", SortOrder.DESC);
//    request.source(query);
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//    SearchHits hits = search.getHits();
//
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }

    // 5. 过滤字段
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
//    String[] excludes = {"age"};
//    String[] includes = {};
//    query.fetchSource(includes, excludes);
//    request.source(query);
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//    SearchHits hits = search.getHits();
//
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }

    // 6. 组合查询
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    SearchSourceBuilder query = new SearchSourceBuilder();
//    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//    query.query(boolQueryBuilder);
//
////    boolQueryBuilder.must(QueryBuilders.matchQuery("age", 20));
////    boolQueryBuilder.mustNot(QueryBuilders.matchQuery("sex", "男"));
//    boolQueryBuilder.should(QueryBuilders.matchQuery("age", "12"));
//    boolQueryBuilder.should(QueryBuilders.matchQuery("age", "20"));
//
//
//    query.query(boolQueryBuilder);
//
//    request.source(query);
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//    SearchHits hits = search.getHits();
//
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }

    // 7. 范围查询
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    SearchSourceBuilder query = new SearchSourceBuilder();
//    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
//
//    rangeQueryBuilder.gte(13);
//    rangeQueryBuilder.lte(40);
//
//    query.query(rangeQueryBuilder);
//
//    request.source(query);
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//    SearchHits hits = search.getHits();
//
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }

    // 8. 模糊查询
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    SearchSourceBuilder query = new SearchSourceBuilder();
//
//    query.query(QueryBuilders.fuzzyQuery("name", "234").fuzziness(Fuzziness.TWO));
//
//    request.source(query);
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//
//
//    SearchHits hits = search.getHits();
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }

    // 9. 高亮查询
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    SearchSourceBuilder query = new SearchSourceBuilder();
//    TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "zhang");
//
//    HighlightBuilder highlightBuilder = new HighlightBuilder();
//    highlightBuilder.preTags("<font color='red'>");
//    highlightBuilder.postTags("</font>");
//    highlightBuilder.field("name");
//
//    query.highlighter(highlightBuilder);
//    query.query(termQueryBuilder);
//
//    request.source(query);
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//
//
//    SearchHits hits = search.getHits();
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }

    // 10. 聚合查询
//    SearchRequest request = new SearchRequest();
//    request.indices("user");
//    SearchSourceBuilder query = new SearchSourceBuilder();
//
//    AggregationBuilder aggregationBuilder = AggregationBuilders.max("maxAge").field("age");
//
//    query.aggregation(aggregationBuilder);
//
//    request.source(query);
//
//    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);
//
//
//    SearchHits hits = search.getHits();
//    System.out.println(hits.getTotalHits());
//    System.out.println(search.getTook());
//
//    for (SearchHit hit : hits) {
//      System.out.println(hit.getSourceAsString());
//    }

    // 11. 分组查询
    SearchRequest request = new SearchRequest();
    request.indices("user");
    SearchSourceBuilder query = new SearchSourceBuilder();

    AggregationBuilder aggregationBuilder = AggregationBuilders.terms("ageGroup").field("age");

    query.aggregation(aggregationBuilder);

    request.source(query);

    SearchResponse search = esClient.search(request, RequestOptions.DEFAULT);


    SearchHits hits = search.getHits();
    System.out.println(hits.getTotalHits());
    System.out.println(search.getTook());

    for (SearchHit hit : hits) {
      System.out.println(hit.getSourceAsString());
    }

    // 关闭ES客户端
    esClient.close();
  }
}
