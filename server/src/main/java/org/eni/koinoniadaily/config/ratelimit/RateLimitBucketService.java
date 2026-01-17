package org.eni.koinoniadaily.config.ratelimit;

import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;

import io.github.bucket4j.Bucket;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RateLimitBucketService {
  
  private final Cache<String, Bucket> cache;

  /**
   * Attempts to consume a single token from the rate-limit bucket associated with the given key.
   *
   * If no bucket exists for the key, a new one is created using the provided supplier and stored in the cache.
   *
   * @param key the identifier for the rate-limit bucket
   * @param bucketSupplier supplier that creates a new Bucket when one is not present in the cache
   * @return {@code true} if a token was successfully consumed, {@code false} otherwise
   */
  public boolean consume(String key, Supplier<Bucket> bucketSupplier) {

    @SuppressWarnings("unused")
    Bucket bucket = cache.get(key, k -> bucketSupplier.get());

    return bucket.tryConsume(1);
  }
}