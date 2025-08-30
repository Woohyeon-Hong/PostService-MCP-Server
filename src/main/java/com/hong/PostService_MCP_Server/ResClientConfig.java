package com.hong.PostService_MCP_Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ResClientConfig {

    private final static String SERVICE_URL = "http://localhost:8080/v2";

    @Bean
    public RestClient restclient() {
        return RestClient.builder()
                .baseUrl(SERVICE_URL)
                .defaultHeader("Origin", "http://localhost:8080")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
