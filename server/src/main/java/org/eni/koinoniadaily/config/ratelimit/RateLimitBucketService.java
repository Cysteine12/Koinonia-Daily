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

  public boolean consume(String key, Supplier<Bucket> bucketSupplier) {

    Bucket bucket = cache.get(key, k -> bucketSupplier.get());

    return bucket.tryConsume(1);
  }
}
