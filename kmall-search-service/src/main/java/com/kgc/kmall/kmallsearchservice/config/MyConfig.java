package com.kgc.kmall.kmallsearchservice.config;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张康硕
 * @create 2021-01-05 11:26
 */
@Configuration
public class MyConfig {
    @Bean
    public JestClient getJestCline() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://192.168.213.131:9200")
                .multiThreaded(true)
                .build());
        return factory.getObject();
    }
}
