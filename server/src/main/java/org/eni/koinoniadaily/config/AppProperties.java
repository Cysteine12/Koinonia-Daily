package org.eni.koinoniadaily.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
  
  private String name = "Koinonia-Daily";

  private int version = 1;

  @NotBlank(message = "JWT secret not set in env")
  @Size(min = 32, message = "JWT secret must be at least 32 characters")
  private String jwtSecret;

  @NotBlank(message = "Seeder user password not set in env")
  @Size(min = 8, message = "Seeder user password minimum length of 8 required")
  private String seederUserPassword;

  private Aws aws = new Aws();

  @Data
  public static class Aws {

    @NotBlank(message = "AWS access key not set in env")
    private String accessKey;

    @NotBlank(message = "AWS secret key not set in env")
    private String secretKey;

    @NotBlank(message = "AWS region not set in env")
    private String region;

    @NotBlank(message = "AWS SES email not set in env")
    private String sesFromEmail;

    @NotBlank(message = "AWS S3 bucket name not set in env")
    private String s3BucketName;
  }
}
