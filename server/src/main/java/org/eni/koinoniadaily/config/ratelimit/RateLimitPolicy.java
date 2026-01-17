package org.eni.koinoniadaily.config.ratelimit;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class RateLimitPolicy {
  
  /**
   * Creates a rate-limiting bucket intended for per-IP throttling.
   *
   * @return a Bucket configured with a single Bandwidth that has a capacity of 100 tokens and a greedy refill of 100 tokens every 1 minute
   */
  public static Bucket ipBucket() {

    Bandwidth bandwidth = Bandwidth.builder()
                            .capacity(100)
                            .refillGreedy(100, Duration.ofMinutes(1))
                            .build();

    return Bucket.builder()
            .addLimit(bandwidth)
            .build();
  }

  /**
   * Creates a rate-limiting Bucket intended for sensitive endpoints.
   *
   * The returned Bucket enforces a limit of 5 tokens with a greedy refill of 5 tokens every 1 minute.
   *
   * @return a Bucket that allows up to 5 tokens and refills 5 tokens per minute
   */
  public static Bucket sensitiveEndpointBucket() {

    Bandwidth bandwidth = Bandwidth.builder()
                            .capacity(5)
                            .refillGreedy(5, Duration.ofMinutes(1))
                            .build();

    return Bucket.builder()
            .addLimit(bandwidth)
            .build();
  } 
}