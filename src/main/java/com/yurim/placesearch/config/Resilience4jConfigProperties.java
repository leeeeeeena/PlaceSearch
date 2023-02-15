package com.yurim.placesearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "resilience4j")
public class Resilience4jConfigProperties {

    private int failureRate;

    private int waitDurationInOpenState;

    private int timeoutDurationSecond;

    private int slidingWindowSize;

}
