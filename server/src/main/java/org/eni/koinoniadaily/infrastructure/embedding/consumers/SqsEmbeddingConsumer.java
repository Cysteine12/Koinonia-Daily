package org.eni.koinoniadaily.infrastructure.embedding.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.infrastructure.embedding.EmbeddingJobHandler;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.embedding.queue.provider", havingValue = "sqs")
public class SqsEmbeddingConsumer implements SchedulingConfigurer {

  private final SqsClient sqsClient;
  private final AppProperties props;
  private final ObjectMapper objectMapper;
  private final EmbeddingJobHandler embeddingJobHandler;

  @Override
  public void configureTasks(ScheduledTaskRegistrar registrar) {
    for (int i = 0; i < props.getAws().getSqs().getWorkerCount(); i++) {
      final int workerId = i + 1;

      registrar.addFixedDelayTask(
          () -> this.poll(workerId),
          Duration.ofSeconds(10)
      );
    }
  }

  public void poll(int workerId) {

    log.debug("SQS worker-{} polling...", workerId);

    List<Message> messages = this.receiveMessages(workerId);

    if (messages.isEmpty()) {
      log.debug("No messages received from SQS");
      return;
    }

    for (Message message : messages) {
        this.processMessage(message, workerId);
    }
  }

  private List<Message> receiveMessages(int workerId) {
    try {
      ReceiveMessageRequest request = ReceiveMessageRequest.builder()
          .queueUrl(props.getAws().getSqs().getQueueUrl())
          .maxNumberOfMessages(1)
          .waitTimeSeconds(20)
          .visibilityTimeout(props.getAws().getSqs().getVisibilityTimeoutSeconds())
          .build();

      return sqsClient.receiveMessage(request).messages();
    } catch (RuntimeException ex) {
      log.error("SQS worker-{} failed to receive messages", workerId, ex);
      return List.of();
    }
  }

  private void processMessage(Message message, int workerId) {
    try {
      EmbeddingJob job = objectMapper.readValue(message.body(), EmbeddingJob.class);

      log.info("SQS worker-{} processing teaching {}", workerId, job.teachingId());

      embeddingJobHandler.handle(job);

      this.deleteMessage(message, workerId);

      log.info("SQS worker-{} message processed and deleted for teaching {}", workerId, job.teachingId());

    } catch (Exception ex) {
      log.error("SQS worker-{} failed to process message {}, It will become visible again after visibility timeout.",
          workerId,
          message.messageId(), ex);
    }
  }

  private void deleteMessage(Message message, int workerId) {
    try {
      DeleteMessageRequest request = DeleteMessageRequest.builder()
          .queueUrl(props.getAws().getSqs().getQueueUrl())
          .receiptHandle(message.receiptHandle())
          .build();

      sqsClient.deleteMessage(request);

    } catch (SqsException ex) {
      log.debug("SQS worker-{} failed to delete message {} after successful processing. Possible redelivery.",
          workerId,
          message.messageId(), ex);
    }
  }
}
