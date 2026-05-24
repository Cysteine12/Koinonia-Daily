package org.eni.koinoniadaily.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

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

  @Valid
  private Email email = new Email();

  @Data
  public static class Email {

    @Pattern(regexp = "ses|mailtrap", message = "Email provider must be 'ses' or 'mailtrap'")
    private String provider = "ses";

    @jakarta.validation.constraints.Email(message = "Email 'from' address has an invalid format")
    @NotBlank(message = "Email 'from' address not set in env")
    private String from;
  }

  @Valid
  private Aws aws = new Aws();

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

    @Valid
    private Sqs sqs = new Sqs();
  }

  @Data
  public static class Sqs {

    @NotBlank(message = "AWS SQS queue URL not set in env")
    private String queueUrl;

    @Positive(message = "AWS SQS visibility timeout must be > 0")
    private int visibilityTimeoutSeconds = 15 * 60;

    @Positive(message = "AWS SQS worker count must be > 0")
    private int workerCount = 2;
  }

  @Valid
  private Mailtrap mailtrap = new Mailtrap();

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

  @NotBlank(message = "OPEN AI api key not set in env")
  private String openaiApiKey;

  @Valid
  private Embedding embedding = new Embedding();

  @Data
  public static class Embedding {

    @Positive(message = "Embedding max chunk token must be > 0")
    private int maxChunkToken = 1200;

    @PositiveOrZero(message = "Embedding overlap token must be >= 0")
    private int overlapToken = 150;

    @AssertTrue(message = "Embedding overlap token must be smaller than max chunk token")
    public boolean isTokenWindowValid() {
      return overlapToken < maxChunkToken;
    }

    @Valid
    private Queue queue = new Queue();

    @Data
    public static class Queue {

      @NotBlank(message = "Embedding queue provider not set in env")
      @Pattern(regexp = "local|sqs", message = "Embedding queue provider must be 'local' or 'sqs'")
      private String provider;
    }

    @Valid
    private Model model = new Model();

    @Data
    public static class Model {

      @NotBlank(message = "Embedding model provider not set in env")
      private String provider;
    }
  }
}
