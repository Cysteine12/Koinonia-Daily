package org.eni.koinonia_daily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@RequiredArgsConstructor
public class SesConfig {

  private final AppProperties props;

  @Bean
  SesClient sesClient() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
      props.getAws().getAccessKey(), 
      props.getAws().getSecretKey()
    );
    
    return SesClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of(props.getAws().getRegion()))
            .build();
  }
}
