package org.eni.koinoniadaily.config.ratelimit;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bucket;

@Configuration
public class RateLimitCacheConfig {
  
  @Bean
  Cache<String, Bucket> rateLimitCache() {
    return Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .maximumSize(100_000)
            .build();
  }
}
