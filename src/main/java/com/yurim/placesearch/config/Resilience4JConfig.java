package com.yurim.placesearch.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Configuration
@RequiredArgsConstructor
public class Resilience4JConfig {

    private final Resilience4jConfigProperties configProperties;


    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfig() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(configProperties.getFailureRate()) //decide circuitBreaker 'open' or not. default 50%
                .waitDurationInOpenState(Duration.ofMillis(configProperties.getWaitDurationInOpenState())) // maintain 'open' time. After DurationInOpenState, circuitBreaker's status modify to 'half-open'
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // count base OR time base . calculate failure rate
                .slidingWindowSize(configProperties.getSlidingWindowSize())
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(configProperties.getTimeoutDurationSecond())) //configure fail
                .build();


        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }

}
