package org.transferservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    @Bean
    public CircuitBreaker accountCircuitBreaker() {

        CircuitBreakerConfig config =
                CircuitBreakerConfig.custom()
                        .failureRateThreshold(50)
                        .slidingWindowSize(4)
                        .minimumNumberOfCalls(4)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .permittedNumberOfCallsInHalfOpenState(1)
                        .build();

        return CircuitBreaker.of("accountService", config);
    }
}