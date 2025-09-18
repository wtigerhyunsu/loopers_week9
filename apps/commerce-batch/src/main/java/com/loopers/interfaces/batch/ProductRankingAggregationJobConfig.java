package com.loopers.interfaces.batch;

import java.time.ZoneId;
import com.loopers.domain.ranking.PeriodResolver;
import com.loopers.interfaces.batch.reader.ProductIdItemReader;
import com.loopers.interfaces.batch.processor.ProductScoreItemProcessor;
import com.loopers.interfaces.batch.writer.TopRankCollectingWriter;
import com.loopers.infrastructure.metrics.ProductMetricsJpaRepository;
import com.loopers.domain.ranking.AggregationRepository;
import com.loopers.domain.ranking.ScoreCalculator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductRankingAggregationJobConfig {
  private static final ZoneId KST = ZoneId.of("Asia/Seoul");
  private final PeriodResolver periodResolver = new PeriodResolver(KST);
  private final JobRepository jobRepository;
  private final PlatformTransactionManager txManager;
  private final JobLauncher launcher;

  public ProductRankingAggregationJobConfig(JobRepository jobRepository, PlatformTransactionManager txManager, JobLauncher launcher) {
    this.jobRepository = jobRepository;
    this.txManager = txManager;
    this.launcher = launcher;
  }

  @Bean
  @StepScope
  public ProductIdItemReader weeklyProductIdReader(ProductMetricsJpaRepository repo,
                                                   @org.springframework.beans.factory.annotation.Value("#{jobParameters['date']}") String date) {
    return new ProductIdItemReader(repo, "weekly", date);
  }

  @Bean
  @StepScope
  public ProductScoreItemProcessor weeklyProductScoreProcessor(ProductMetricsJpaRepository repo,
                                                               ScoreCalculator calculator,
                                                               @org.springframework.beans.factory.annotation.Value("#{jobParameters['date']}") String date) {
    return new ProductScoreItemProcessor(repo, calculator, "weekly", date);
  }

  @Bean
  @StepScope
  public TopRankCollectingWriter weeklyTopRankWriter(AggregationRepository repository,
                                                     @org.springframework.beans.factory.annotation.Value("#{jobParameters['date']}") String date) {
    return new TopRankCollectingWriter(repository, "weekly", date);
  }

  @Bean
  public Step weeklyAggregationStep(ProductIdItemReader weeklyProductIdReader,
                                    ProductScoreItemProcessor weeklyProductScoreProcessor,
                                    TopRankCollectingWriter weeklyTopRankWriter) {
    return new StepBuilder("weeklyAggregationStep", jobRepository)
        .<Long, com.loopers.application.ranking.ProductScore>chunk(1000, txManager)
        .reader(weeklyProductIdReader)
        .processor(weeklyProductScoreProcessor)
        .writer(weeklyTopRankWriter)
        .listener(weeklyTopRankWriter)
        .build();
  }

  @Bean
  @StepScope
  public ProductIdItemReader monthlyProductIdReader(ProductMetricsJpaRepository repo,
                                                   @org.springframework.beans.factory.annotation.Value("#{jobParameters['date']}") String date) {
    return new ProductIdItemReader(repo, "monthly", date);
  }

  @Bean
  @StepScope
  public ProductScoreItemProcessor monthlyProductScoreProcessor(ProductMetricsJpaRepository repo,
                                                               ScoreCalculator calculator,
                                                               @org.springframework.beans.factory.annotation.Value("#{jobParameters['date']}") String date) {
    return new ProductScoreItemProcessor(repo, calculator, "monthly", date);
  }

  @Bean
  @StepScope
  public TopRankCollectingWriter monthlyTopRankWriter(AggregationRepository repository,
                                                     @org.springframework.beans.factory.annotation.Value("#{jobParameters['date']}") String date) {
    return new TopRankCollectingWriter(repository, "monthly", date);
  }

  @Bean
  public Step monthlyAggregationStep(ProductIdItemReader monthlyProductIdReader,
                                     ProductScoreItemProcessor monthlyProductScoreProcessor,
                                     TopRankCollectingWriter monthlyTopRankWriter) {
    return new StepBuilder("monthlyAggregationStep", jobRepository)
        .<Long, com.loopers.application.ranking.ProductScore>chunk(1000, txManager)
        .reader(monthlyProductIdReader)
        .processor(monthlyProductScoreProcessor)
        .writer(monthlyTopRankWriter)
        .listener(monthlyTopRankWriter)
        .build();
  }

  @Bean
  public Job productRankingAggregationJob(Step weeklyAggregationStep,
                                          Step monthlyAggregationStep) {
    return new JobBuilder("productRankingAggregationJob", jobRepository)
        .start(weeklyAggregationStep)
        .next(monthlyAggregationStep)
        .build();
  }

}
