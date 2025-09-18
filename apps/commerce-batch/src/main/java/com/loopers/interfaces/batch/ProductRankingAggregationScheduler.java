package com.loopers.interfaces.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProductRankingAggregationScheduler {
  private final JobLauncher launcher;
  private final Job job;

  public ProductRankingAggregationScheduler(JobLauncher launcher,
                                            @Qualifier("productRankingAggregationJob") Job job) {
    this.launcher = launcher;
    this.job = job;
  }

  @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
  public void runWeekly() throws Exception {
    launcher.run(job, new JobParameters());
  }

  @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Seoul")
  public void runMonthly() throws Exception {
    launcher.run(job, new JobParameters());
  }
}

