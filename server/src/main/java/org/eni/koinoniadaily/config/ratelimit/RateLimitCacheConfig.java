package org.eni.koinoniadaily.config.ratelimit;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bucket;

@Configuration
public class RateLimitCacheConfig {
  
  /**
   * Creates a Caffeine-backed cache for storing rate-limiting buckets keyed by string identifiers.
   *
   * The cache expires entries 10 minutes after the last access and is capped at 100,000 entries.
   *
   * @return a Cache<String, Bucket> that holds rate-limiting buckets with the configured eviction policy
   */
  @Bean
  Cache<String, Bucket> rateLimitCache() {
    return Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .maximumSize(100_000)
            .build();
  }
}