package com.shsb.atfbook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${aladin.host}")
    public String aladinHost;

    @Value("${gpt.host}")
    public String gptHost;

    @Value("${gpt.api_key}")
    private String gptApiKey;

    @Bean
    public RestClient aladinApi() {
        return RestClient.create(aladinHost);
    }

    @Bean
    public RestClient gptApi() {
        return RestClient.builder()
                .baseUrl(gptHost)
                .defaultHeader("Authorization", "Bearer " + gptApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
