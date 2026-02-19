package org.eni.koinoniadaily.config;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app")
@Data
@Validated
public class AppProperties {
  
  private String name = "Koinonia-Daily";

  private int version = 1;

  @NotBlank(message = "JWT secret not set in env")
  @Size(min = 32, message = "JWT secret must be at least 32 characters")
  private String jwtSecret;

  @NotBlank(message = "Seeder user password not set in env")
  @Size(min = 8, message = "Seeder user password minimum length of 8 required")
  private String seederUserPassword;

  private Email email = new Email();
  private Aws aws = new Aws();
  private Mailtrap mailtrap = new Mailtrap();

  @Data
  public static class Email {

    private String provider = "mailtrap";

    @NotBlank(message = "Email address not set in env")
    private String from;
  }

  @Data
  public static class Aws {

    @NotBlank(message = "AWS access key not set in env")
    private String accessKey;

    @NotBlank(message = "AWS secret key not set in env")
    private String secretKey;

    @NotBlank(message = "AWS region not set in env")
    private String region;

    @NotBlank(message = "AWS S3 bucket name not set in env")
    private String s3BucketName;
  }

  @Data
  public static class Mailtrap {

    @NotBlank(message = "Mailtrap host not set in env")
    private String host;

    @Positive(message = "Mailtrap port incorrect in env")
    private int port;

    @NotBlank(message = "Mailtrap username not set in env")
    private String username;

    @NotBlank(message = "Mailtrap password not set in env")
    private String password;
  }
}
