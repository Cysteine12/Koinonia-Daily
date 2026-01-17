package org.eni.koinoniadaily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class S3Config {
  
  private final AppProperties props;

  @Bean
  S3Client sesClient() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        props.getAws().getAccessKey(), 
        props.getAws().getSecretKey()
    );
    
    return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of(props.getAws().getRegion()))
            .build();
  }

  @Bean
  S3Presigner s3Presigner() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        props.getAws().getAccessKey(), 
        props.getAws().getSecretKey()
    );
    
    return S3Presigner.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of(props.getAws().getRegion()))
            .build();
  }
}
