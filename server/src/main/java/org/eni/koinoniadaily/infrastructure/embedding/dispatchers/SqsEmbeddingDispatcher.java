package org.eni.koinoniadaily.infrastructure.embedding.dispatchers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.exceptions.EmbeddingException;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.embedding.queue.provider", havingValue = "sqs")
@Qualifier("sqs")
public class SqsEmbeddingDispatcher implements EmbeddingJobDispatcher {

  private final SqsClient sqsClient;
  private final AppProperties props;
  private final ObjectMapper objectMapper;

  @Override
  public void dispatch(EmbeddingJob job) {
    try {
      String messageBody = objectMapper.writeValueAsString(job);

      SendMessageRequest request = SendMessageRequest.builder()
          .queueUrl(props.getAws().getSqs().getQueueUrl())
          .messageBody(messageBody)
          .build();

      sqsClient.sendMessage(request);

      log.info("SQS embedding job dispatched for teaching {}", job.teachingId());

    } catch (RuntimeException ex) {
      log.error("SQS embedding job dispatch failed for teaching {}", job.teachingId(), ex);
      throw new EmbeddingException("Failed to dispatch SQS embedding job to queue", ex);
    }
  }
}
