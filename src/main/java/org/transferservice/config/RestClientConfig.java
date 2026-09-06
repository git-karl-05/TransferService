package org.transferservice.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    private final Logger log = LoggerFactory.getLogger(RestClientConfig.class);

    @Bean
    public RestClient accountRestClient(
            @Value("${account.service.url}") String accountServiceUrl
    ) {

        log.info("Account Service URL: {}", accountServiceUrl);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        return RestClient.builder()
                .baseUrl(accountServiceUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @Bean
    public RestClient fraudRestClient(
            @Value("${fraud.service.url}") String fraudServiceUrl
    ) {

        log.info("Fraud Service URL: {}",fraudServiceUrl);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        return RestClient.builder()
                .baseUrl(fraudServiceUrl)
                .requestFactory(requestFactory)
                .build();
    }
}
