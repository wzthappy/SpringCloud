package cn.itcast.hotel.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
  
  @Bean
  public RestHighLevelClient client() {
    // ES 的 client
    return new RestHighLevelClient(RestClient.builder(
        HttpHost.create("http://192.168.200.130:9200")
    ));
  }
}
