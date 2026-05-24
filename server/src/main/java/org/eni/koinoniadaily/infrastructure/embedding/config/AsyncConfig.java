package org.eni.koinoniadaily.infrastructure.embedding.config;

import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.config.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class AsyncConfig {

  private final AppProperties props;

  @Bean(name = "embeddingExecutor")
  public Executor embeddingExecutor() {

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(4);
    executor.setQueueCapacity(50);
    executor.setThreadNamePrefix("embedding-");
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(60);
    executor.setRejectedExecutionHandler(
        new ThreadPoolExecutor.AbortPolicy()
    );
    executor.initialize();

    return executor;
  }

  @Bean(name = "taskScheduler")
  public TaskScheduler taskScheduler() {

    final int noOfWorkers = props.getAws().getSqs().getWorkerCount();

    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

    scheduler.setPoolSize(noOfWorkers + 2);
    scheduler.setThreadNamePrefix("sqs-worker-");
    scheduler.setWaitForTasksToCompleteOnShutdown(true);
    scheduler.setAwaitTerminationSeconds(120);
    scheduler.setRejectedExecutionHandler(
        new ThreadPoolExecutor.AbortPolicy()
    );
    scheduler.initialize();

    return scheduler;
  }
}
