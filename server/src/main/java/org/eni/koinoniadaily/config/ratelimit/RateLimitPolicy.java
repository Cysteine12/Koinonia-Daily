package org.eni.koinoniadaily.config.ratelimit;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class RateLimitPolicy {
  
  public static Bucket ipBucket() {

    Bandwidth bandwidth = Bandwidth.builder()
                            .capacity(100)
                            .refillGreedy(1100, Duration.ofMinutes(1))
                            .build();

    return Bucket.builder()
            .addLimit(bandwidth)
            .build();
  }

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
