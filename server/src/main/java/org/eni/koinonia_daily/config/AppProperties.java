package org.eni.koinonia_daily.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
  
  private String name = "Koinonia-Daily";
  private int version = 1;
  private String jwtSecret;

  private Aws aws = new Aws();

  @Data
  public static class Aws {
    private String accessKey;
    private String secretKey;
    private String region;
    private String sesFromEmail;
    private String s3BucketName;
  }
}
