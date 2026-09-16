package org.transferservice.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    private final Logger log = LoggerFactory.getLogger(RestClientConfig.class);

    @Bean
    public RestClient accountRestClient(
            @Value("${account.service.url}") String accountServiceUrl,
            OAuth2ClientHttpRequestInterceptor oauth2Interceptor
    ) {

        log.info("Account Service URL: {}", accountServiceUrl);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        return RestClient.builder()
                .baseUrl(accountServiceUrl)
                .requestFactory(requestFactory)
                .requestInterceptor(oauth2Interceptor)
                .build();

    }

    @Bean
    public RestClient fraudRestClient(
            @Value("${fraud.service.url}") String fraudServiceUrl,
            OAuth2ClientHttpRequestInterceptor oauth2Interceptor
    ) {

        log.info("Fraud Service URL: {}",fraudServiceUrl);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(Duration.ofSeconds(10));
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        return RestClient.builder()
                .baseUrl(fraudServiceUrl)
                .requestFactory(requestFactory)
                .requestInterceptor(oauth2Interceptor)
                .build();
    }

    @Bean
    public OAuth2ClientHttpRequestInterceptor oAuth2ClientHttpRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {

        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        interceptor.setClientRegistrationIdResolver(request -> "keycloak");


        return interceptor;
    }
}
