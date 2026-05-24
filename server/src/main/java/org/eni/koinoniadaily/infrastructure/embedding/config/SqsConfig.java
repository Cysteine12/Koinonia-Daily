package org.eni.koinoniadaily.infrastructure.embedding.config;

import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.config.AppProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@ConditionalOnProperty(name = "app.embedding.queue.provider", havingValue = "sqs")
@RequiredArgsConstructor
public class SqsConfig {

  private final AppProperties props;

  @Bean
  SqsClient sqsClient() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        props.getAws().getAccessKey(),
        props.getAws().getSecretKey()
    );

    return SqsClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .region(Region.of(props.getAws().getRegion()))
        .build();
  }
}
